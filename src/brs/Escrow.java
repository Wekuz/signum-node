package brs;

import brs.db.SignumKey;

import java.util.Collection;

public class Escrow {

  public enum DecisionType {
    UNDECIDED,
    RELEASE,
    REFUND,
    SPLIT
  }

  public static String decisionToString(DecisionType decision) {
    switch (decision) {
      case UNDECIDED:
        return "undecided";
      case RELEASE:
        return "release";
      case REFUND:
        return "refund";
      case SPLIT:
        return "split";
    }

    return null;
  }

  public static DecisionType stringToDecision(String decision) {
    switch (decision) {
      case "undecided":
        return DecisionType.UNDECIDED;
      case "release":
        return DecisionType.RELEASE;
      case "refund":
        return DecisionType.REFUND;
      case "split":
        return DecisionType.SPLIT;
      default:
        return null;
    }
  }

  public static Byte decisionToByte(DecisionType decision) {
    switch (decision) {
      case UNDECIDED:
        return 0;
      case RELEASE:
        return 1;
      case REFUND:
        return 2;
      case SPLIT:
        return 3;
      default:
        return null;
    }
  }

  public static DecisionType byteToDecision(Byte decision) {
    switch (decision) {
      case 0:
        return DecisionType.UNDECIDED;
      case 1:
        return DecisionType.RELEASE;
      case 2:
        return DecisionType.REFUND;
      case 3:
        return DecisionType.SPLIT;
      default:
        return null;
    }
  }

  public static class Decision {

    public final Long escrowId;
    public final Long accountId;
    public final SignumKey dbKey;
    private DecisionType decisionType;

    public Decision(SignumKey dbKey, Long escrowId, Long accountId, DecisionType decisionType) {
      this.dbKey = dbKey;
      this.escrowId = escrowId;
      this.accountId = accountId;
      this.decisionType = decisionType;
    }


    public Long getEscrowId() {
      return this.escrowId;
    }

    public Long getAccountId() {
      return this.accountId;
    }

    public DecisionType getDecision() {
      return this.decisionType;
    }

    public void setDecision(DecisionType decision) {
      this.decisionType = decision;
    }
  }

  public final Long senderId;
  public final Long recipientId;
  public final Long id;
  public final SignumKey dbKey;
  public final Long amountNQT;
  public final int requiredSigners;
  public final int deadline;
  public final DecisionType deadlineAction;

  public Escrow(SignumKey dbKey, Account sender,
                Account recipient,
                Long id,
                Long amountNQT,
                int requiredSigners,
                int deadline,
                DecisionType deadlineAction) {
    this.dbKey = dbKey;
    this.senderId = sender.getId();
    this.recipientId = recipient.getId();
    this.id = id;
    this.amountNQT = amountNQT;
    this.requiredSigners = requiredSigners;
    this.deadline = deadline;
    this.deadlineAction = deadlineAction;
  }

  protected Escrow(Long id, Long senderId, Long recipientId, SignumKey dbKey, Long amountNQT,
                   int requiredSigners, int deadline, DecisionType deadlineAction) {
    this.senderId = senderId;
    this.recipientId = recipientId;
    this.id = id;
    this.dbKey = dbKey;
    this.amountNQT = amountNQT;
    this.requiredSigners = requiredSigners;
    this.deadline = deadline;
    this.deadlineAction = deadlineAction;
  }

  public Long getSenderId() {
    return senderId;
  }

  public Long getAmountNQT() {
    return amountNQT;
  }

  public Long getRecipientId() {
    return recipientId;
  }

  public Long getId() {
    return id;
  }

  public int getRequiredSigners() {
    return requiredSigners;
  }

  public Collection<Decision> getDecisions() {
    return Signum.getStores().getEscrowStore().getDecisions(id);
  }

  public int getDeadline() {
    return deadline;
  }

  public DecisionType getDeadlineAction() {
    return deadlineAction;
  }
}
