package brs.peer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.google.protobuf.ProtocolStringList;

import brs.Block;
import brs.Blockchain;
import brs.BlockchainProcessor;
import brs.TransactionProcessor;
import brs.peer.proto.AttachmentProto;
import brs.peer.proto.PeersServiceGrpc;
import brs.peer.proto.TransactionProto;
import brs.peer.proto.addPeersReq;
import brs.peer.proto.addPeersRes;
import brs.peer.proto.getBlocksFromHeightReq;
import brs.peer.proto.getBlocksFromHeightRes;
import brs.peer.proto.getCumulativeDifficultyReq;
import brs.peer.proto.getCumulativeDifficultyRes;
import brs.peer.proto.getInfoReq;
import brs.peer.proto.getInfoRes;
import brs.peer.proto.getMilestoneBlockIdReq;
import brs.peer.proto.getMilestoneBlockIdRes;
import brs.peer.proto.getNextBlockIdsReq;
import brs.peer.proto.getNextBlockIdsRes;
import brs.peer.proto.getNextBlocksReq;
import brs.peer.proto.getNextBlocksRes;
import brs.peer.proto.getPeersReq;
import brs.peer.proto.getPeersRes;
import brs.peer.proto.getUnconfirmedTransactionsReq;
import brs.peer.proto.getUnconfirmedTransactionsRes;
import brs.peer.proto.processBlockReq;
import brs.peer.proto.processBlockRes;
import brs.peer.proto.processTransactionsReq;
import brs.peer.proto.processTransactionsRes;
import brs.props.PropertyService;
import brs.services.AccountService;
import brs.services.TimeService;
import brs.util.Convert;
import brs.util.JSON;
import io.grpc.stub.StreamObserver;

public class PeersServiceImpl extends PeersServiceGrpc.PeersServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(PeersServiceImpl.class);

    PeerInterceptor interceptor;

    TimeService timeService;
    Blockchain blockchain;

    public PeersServiceImpl(PeerInterceptor interceptor, TimeService timeService, AccountService accountService,
            Blockchain blockchain,
            TransactionProcessor transactionProcessor,
            BlockchainProcessor blockchainProcessor,
            PropertyService propertyService) {
        this.interceptor = interceptor;
        this.timeService = timeService;
        this.blockchain = blockchain;
    }

    @Override
    public void addPeers(addPeersReq req, StreamObserver<addPeersRes> responseObserver) {
        ProtocolStringList peers = req.getPeersList();
        if (!peers.isEmpty() && Peers.getMorePeers) {
            for (String announcedAddress : peers) {
                Peers.addPeer(announcedAddress);
            }
        }
        responseObserver.onNext(addPeersRes.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCumulativeDifficulty(getCumulativeDifficultyReq req,
            StreamObserver<getCumulativeDifficultyRes> responseObserver) {
        Block lastBlock = blockchain.getLastBlock();
        responseObserver.onNext(getCumulativeDifficultyRes.newBuilder()
                .setCumulativeDifficulty(lastBlock.getCumulativeDifficulty().toString())
                .setBlockchainHeight(lastBlock.getHeight()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getInfo(getInfoReq req, StreamObserver<getInfoRes> responseObserver) {
        PeerImpl peerImpl = (PeerImpl) interceptor.peer.get();
        String announcedAddress = req.getAnnouncedAddress();
        if (announcedAddress != null) {
            announcedAddress = announcedAddress.trim();
            if (!announcedAddress.isEmpty()) {
                if (peerImpl.getAnnouncedAddress() != null
                        && !announcedAddress.equals(peerImpl.getAnnouncedAddress())) {
                    // force verification of changed announced address
                    peerImpl.setState(Peer.State.NON_CONNECTED);
                }
                peerImpl.setAnnouncedAddress(announcedAddress);
            }
        }
        String application = req.getApplication();
        if (application == null) {
            application = "?";
        }
        peerImpl.setApplication(application.trim());

        String version = req.getVersion();
        if (version == null) {
            version = "?";
        }
        peerImpl.setVersion(version.trim());

        String platform = req.getPlatform();
        if (platform == null) {
            platform = "?";
        }
        peerImpl.setPlatform(platform.trim());

        String networkName = req.getNetworkName();
        peerImpl.setNetworkName(networkName);

        peerImpl.setShareAddress(req.getShareAddress());
        peerImpl.setLastUpdated(timeService.getEpochTime());

        Peers.notifyListeners(peerImpl, Peers.Event.ADDED_ACTIVE_PEER);

        JsonObject responseJson = Peers.myPeerInfoResponse.getAsJsonObject();

        responseObserver
                .onNext((responseJson.has("announcedAddress") ? getInfoRes.newBuilder().setAnnouncedAddress(
                        JSON.getAsString(responseJson.get("announcedAddress"))) : getInfoRes.newBuilder())
                        .setApplication(JSON.getAsString(responseJson.get("application")))
                        .setVersion(JSON.getAsString(responseJson.get("version")))
                        .setPlatform(JSON.getAsString(responseJson.get("platform")))
                        .setShareAddress(Boolean.TRUE.equals(
                                JSON.getAsBoolean(responseJson.get("shareAddress"))))
                        .setNetworkName(JSON.getAsString(responseJson.get("networkName")))
                        .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getMilestoneBlockId(getMilestoneBlockIdReq req,
            StreamObserver<getMilestoneBlockIdRes> responseObserver) {
        var response = getMilestoneBlockIdRes.newBuilder();
        try {

            List<String> milestoneBlockIds = new ArrayList<>();

            String lastBlockIdString = req.getLastBlockId();
            if (lastBlockIdString != null) {
                long lastBlockId = Convert.parseUnsignedLong(lastBlockIdString);
                long myLastBlockId = blockchain.getLastBlock().getId();
                if (myLastBlockId == lastBlockId || blockchain.hasBlock(lastBlockId)) {
                    milestoneBlockIds.add(lastBlockIdString);
                    response.addAllMilestoneBlockIds(milestoneBlockIds);
                    if (myLastBlockId == lastBlockId) {
                        response.setLast(true);
                    }
                    responseObserver.onNext(response.build());
                    responseObserver.onCompleted();
                    return;
                }
            }

            long blockId;
            int height;
            int jump;
            int limit = 10;
            int blockchainHeight = blockchain.getHeight();
            String lastMilestoneBlockIdString = req.getLastMilestoneBlockId();
            if (lastMilestoneBlockIdString != null) {
                Block lastMilestoneBlock = blockchain.getBlock(Convert.parseUnsignedLong(lastMilestoneBlockIdString));
                if (lastMilestoneBlock == null) {
                    throw new IllegalStateException("Don't have block " + lastMilestoneBlockIdString);
                }
                height = lastMilestoneBlock.getHeight();
                jump = Math.min(1440, Math.max(blockchainHeight - height, 1));
                height = Math.max(height - jump, 0);
            } else if (lastBlockIdString != null) {
                height = blockchainHeight;
                jump = 10;
            } else {
                interceptor.peer.get().blacklist("GetMilestoneBlockIds");
                response.setError("Old getMilestoneBlockIds protocol not supported, please upgrade");
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
                return;
            }
            blockId = blockchain.getBlockIdAtHeight(height);

            while (height > 0 && limit-- > 0) {
                milestoneBlockIds.add(Convert.toUnsignedLong(blockId));
                blockId = blockchain.getBlockIdAtHeight(height);
                height = height - jump;
            }
            response.addAllMilestoneBlockIds(milestoneBlockIds);

        } catch (RuntimeException e) {
            logger.debug(e.toString());
            response.setError(e.toString());
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getNextBlockIds(getNextBlockIdsReq req,
            StreamObserver<getNextBlockIdsRes> responseObserver) {
        var response = getNextBlockIdsRes.newBuilder();
        Collection<Long> ids = blockchain.getBlockIdsAfter(req.getBlockId(), 100);

        for (Long id : ids) {
            response.addNextBlockIds(id);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getBlocksFromHeight(getBlocksFromHeightReq req,
            StreamObserver<getBlocksFromHeightRes> responseObserver) {
        var response = getBlocksFromHeightRes.newBuilder();
        int blockHeight = req.getHeight();
        int numBlocks = req.getNumBlocks();

        if (numBlocks <= 0) {
            numBlocks = 100;
        }

        // small failsafe
        if (numBlocks < 1 || numBlocks > 1400) {
            numBlocks = 100;
        }
        if (blockHeight < 0) {
            blockHeight = 0;
        }

        long blockId = blockchain.getBlockIdAtHeight(blockHeight);
        Collection<? extends Block> blocks = blockchain.getBlocksAfter(blockId, numBlocks);
        for (Block nextBlock : blocks) {
            List<TransactionProto> nextBlockTxs = new ArrayList<>();
            for (brs.Transaction nextBlockTx : nextBlock.getTransactions()) {
                var nextBlockTxAttachment = AttachmentProto.newBuilder()
                        .build();
                // BUG?: (grpc) referencedTransactionFullHash might be null
                nextBlockTxs.add(TransactionProto.newBuilder()
                        .setType(nextBlockTx.getType().getType())
                        .setSubtype(nextBlockTx.getType().getSubtype())
                        .setTimestamp(nextBlockTx.getTimestamp())
                        .setDeadline(nextBlockTx.getDeadline())
                        .setSenderPublicKey(ByteString.copyFrom(nextBlockTx.getSenderPublicKey()))
                        .setAmountNQT(nextBlockTx.getAmountNqt())
                        .setFeeNQT(nextBlockTx.getFeeNqt())
                        .setReferencedTransactionFullHash(nextBlockTx.getReferencedTransactionFullHash())
                        .setSignature(ByteString.copyFrom(nextBlockTx.getSignature()))
                        .setVersion(nextBlockTx.getVersion())
                        .setAttachment(nextBlockTxAttachment)
                        .setRecipient(nextBlockTx.getRecipientId())
                        .setEcBlockHeight(nextBlockTx.getEcBlockHeight())
                        .setEcBlockId(nextBlockTx.getEcBlockId())
                        .setCashBackId(nextBlockTx.getCashBackId())
                        .build());
            }
            var protoBlock = brs.peer.proto.BlockProto.newBuilder()
                    .setVersion(nextBlock.getVersion())
                    .setTimestamp(nextBlock.getTimestamp())
                    .setPreviousBlock(nextBlock.getPreviousBlockId())
                    .setTotalAmountNQT(nextBlock.getTotalAmountNqt())
                    .setTotalFeeNQT(nextBlock.getTotalFeeNqt())
                    .setTotalFeeCashBackNQT(nextBlock.getTotalFeeCashBackNqt())
                    .setTotalFeeBurntNQT(nextBlock.getTotalFeeBurntNqt())
                    .setPayloadLength(nextBlock.getPayloadLength())
                    .setPayloadHash(ByteString.copyFrom(nextBlock.getPayloadHash()))
                    .setGeneratorPublicKey(ByteString.copyFrom(nextBlock.getGeneratorPublicKey()))
                    .setGenerationSignature(ByteString.copyFrom(nextBlock.getGenerationSignature()))
                    .setBlockSignature(ByteString.copyFrom(nextBlock.getBlockSignature()))
                    .setPreviousBlockHash(ByteString.copyFrom(nextBlock.getPreviousBlockHash()))
                    .setNonce(nextBlock.getNonce())
                    .setBaseTarget(nextBlock.getBaseTarget())
                    .setBlockATs(ByteString.copyFrom(nextBlock.getBlockAts()))
                    .addAllTransactions(nextBlockTxs)
                    .build();
            response.addNextBlocks(protoBlock);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getNextBlocks(getNextBlocksReq req,
            StreamObserver<getNextBlocksRes> responseObserver) {

    }

    @Override
    public void getPeers(getPeersReq req,
            StreamObserver<getPeersRes> responseObserver) {
        List<String> peers = new ArrayList<>();
        for (Peer otherPeer : Peers.getAllPeers()) {

            if (!otherPeer.isBlacklisted() && otherPeer.getAnnouncedAddress() != null
                    && otherPeer.getState() == Peer.State.CONNECTED && otherPeer.shareAddress()) {

                peers.add(otherPeer.getAnnouncedAddress());

            }

        }

        responseObserver.onNext(getPeersRes.newBuilder().addAllPeers(peers).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUnconfirmedTransactions(getUnconfirmedTransactionsReq req,
            StreamObserver<getUnconfirmedTransactionsRes> responseObserver) {

    }

    @Override
    public void processBlock(processBlockReq req,
            StreamObserver<processBlockRes> responseObserver) {

    }

    @Override
    public void processTransactions(processTransactionsReq req,
            StreamObserver<processTransactionsRes> responseObserver) {

    }
}
