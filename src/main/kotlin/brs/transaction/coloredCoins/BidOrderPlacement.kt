package brs.transaction.coloredCoins

import brs.Account
import brs.Attachment
import brs.DependencyProvider
import brs.Transaction
import brs.util.convert.safeMultiply
import brs.util.logging.safeTrace
import com.google.gson.JsonObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

class BidOrderPlacement(dp: DependencyProvider) : OrderPlacement(dp) {

    override val subtype = SUBTYPE_COLORED_COINS_BID_ORDER_PLACEMENT

    override val description = "Bid Order Placement"

    override fun parseAttachment(
        buffer: ByteBuffer,
        transactionVersion: Byte
    ): Attachment.ColoredCoinsBidOrderPlacement {
        return Attachment.ColoredCoinsBidOrderPlacement(dp, buffer, transactionVersion)
    }

    override fun parseAttachment(attachmentData: JsonObject): Attachment.ColoredCoinsBidOrderPlacement {
        return Attachment.ColoredCoinsBidOrderPlacement(dp, attachmentData)
    }

    override fun applyAttachmentUnconfirmed(transaction: Transaction, senderAccount: Account): Boolean {
        logger.safeTrace { "TransactionType BID_ORDER_PLACEMENT" }
        val totalAmountPlanck = calculateAttachmentTotalAmountPlanck(transaction)
        if (senderAccount.unconfirmedBalancePlanck >= totalAmountPlanck) {
            dp.accountService.addToUnconfirmedBalancePlanck(senderAccount, -totalAmountPlanck)
            return true
        }
        return false
    }

    public override fun calculateAttachmentTotalAmountPlanck(transaction: Transaction): Long {
        val attachment = transaction.attachment as Attachment.ColoredCoinsBidOrderPlacement
        return attachment.quantity.safeMultiply(attachment.pricePlanck)
    }

    override fun applyAttachment(
        transaction: Transaction,
        senderAccount: Account,
        recipientAccount: Account?
    ) {
        val attachment = transaction.attachment as Attachment.ColoredCoinsBidOrderPlacement
        if (dp.assetExchange.getAsset(attachment.assetId) != null) {
            dp.assetExchange.addBidOrder(transaction, attachment)
        }
    }

    override fun undoAttachmentUnconfirmed(transaction: Transaction, senderAccount: Account) {
        dp.accountService.addToUnconfirmedBalancePlanck(senderAccount, calculateAttachmentTotalAmountPlanck(transaction))
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BidOrderPlacement::class.java)
    }
}
