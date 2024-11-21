package brs.peer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

public class PeerInterceptor implements ServerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(PeerInterceptor.class);

    public final Context.Key<Peer> peer = Context.key("peer");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        Peer peer = null;

        try {
            peer = Peers.addPeer(call.getAttributes()
                    .get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR).toString().substring(1), null);
            if (peer == null) {
                call.close(Status.FAILED_PRECONDITION, new Metadata());
                return new ServerCall.Listener<ReqT>() {
                };

            }
            if (peer.isBlacklisted()) {
                call.close(Status.PERMISSION_DENIED, new Metadata());
                return new ServerCall.Listener<ReqT>() {
                };
            }

            // TODO: (grpc) R/W stats

            if (peer.isState(Peer.State.DISCONNECTED)) {
                peer.setState(Peer.State.CONNECTED);
                if (peer.getAnnouncedAddress() != null) {
                    Peers.updateAddress(peer);
                }
            }
        } catch (RuntimeException e) {
            logger.debug("Error processing gRPC request", e);
            call.close(Status.INTERNAL.withCause(e)
                    .withDescription("error processing"), new Metadata());
        }

        Context context = Context.current().withValue(this.peer, peer);
        return Contexts.interceptCall(context, call, headers, next);
    }
}
