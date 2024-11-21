package brs.peer;

import brs.Version;
import brs.peer.proto.PeersServiceGrpc.PeersServiceBlockingStub;

import java.util.HashMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

//TODO: Create JavaDocs and remove this
@SuppressWarnings({ "checkstyle:MissingJavadocTypeCheck", "checkstyle:MissingJavadocMethodCheck" })
public interface Peer extends Comparable<Peer> {

    void connect(int currentTime);

    enum State {
        NON_CONNECTED, CONNECTED, DISCONNECTED;
    }

    String getPeerAddress();

    String getAnnouncedAddress();

    State getState();

    void updateUploadedVolume(long volume);

    Version getVersion();

    String getApplication();

    String getPlatform();

    String getNetworkName();

    String getSoftware();

    boolean shareAddress();

    int getPort();

    boolean isWellKnown();

    boolean isRebroadcastTarget();

    boolean isBlacklisted();

    boolean isAtLeastMyVersion();

    boolean isHigherOrEqualVersionThan(Version version);

    void blacklist(Exception cause, String description);

    void blacklist(String description);

    void blacklist();

    void unBlacklist();

    void updateBlacklistedStatus(long curTime);

    void remove();

    boolean isState(State cmpState);

    void setState(State state);

    long getDownloadedVolume();

    void updateDownloadedVolume(long volume);

    long getUploadedVolume();

    int getLastUpdated();

    JsonObject send(JsonElement request);

    PeersServiceBlockingStub createGrpcStub();

    static boolean isHigherOrEqualVersion(Version ourVersion, Version possiblyLowerVersion) {
        if (ourVersion == null || possiblyLowerVersion == null) {
            return false;
        }

        return possiblyLowerVersion.isGreaterThanOrEqualTo(ourVersion);
    }
}
