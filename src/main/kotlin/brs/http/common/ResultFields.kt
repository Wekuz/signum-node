package brs.http.common

object ResultFields {
    const val ERROR_CODE_RESPONSE = "errorCode"
    const val ERROR_DESCRIPTION_RESPONSE = "errorDescription"
    const val DECRYPTED_MESSAGE_RESPONSE = "decryptedMessage"
    const val BALANCE_PLANCK_RESPONSE = "balanceNQT"
    const val BALANCE_QUANTITY_RESPONSE = "balanceQNT"
    const val UNCONFIRMED_BALANCE_PLANCK_RESPONSE = "unconfirmedBalanceNQT"
    const val UNCONFIRMED_BALANCE_QUANTITY_RESPONSE = "unconfirmedBalanceQNT"
    const val EFFECTIVE_BALANCE_PLANCK_RESPONSE = "effectiveBalanceNXT"
    const val FORGED_BALANCE_PLANCK_RESPONSE = "forgedBalanceNQT"
    const val GUARANTEED_BALANCE_PLANCK_RESPONSE = "guaranteedBalanceNQT"
    const val TRANSACTION_RESPONSE = "transaction"
    const val FULL_HASH_RESPONSE = "fullHash"
    const val ERROR_RESPONSE = "error"
    const val ASK_ORDERS_RESPONSE = "askOrders"
    const val ASK_ORDER_IDS_RESPONSE = "askOrderIds"
    const val BLOCKS_RESPONSE = "blocks"
    const val DONE_RESPONSE = "done"
    const val SCAN_TIME_RESPONSE = "scanTime"
    const val BROADCASTED_RESPONSE = "broadcasted"
    const val NUMBER_PEERS_SENT_TO_RESPONSE = "numberPeersSentTo"
    const val UNSIGNED_TRANSACTION_BYTES_RESPONSE = "unsignedTransactionBytes"
    const val TRANSACTION_JSON_RESPONSE = "transactionJSON"
    const val TRANSACTION_BYTES_RESPONSE = "transactionBytes"
    const val SIGNATURE_RESPONSE = "signature"
    const val SIGNATURE_HASH_RESPONSE = "signatureHash"
    const val VERIFY_RESPONSE = "verify"
    const val VALIDATE_RESPONSE = "validate"
    const val PUBLIC_KEY_RESPONSE = "publicKey"
    const val NAME_RESPONSE = "name"
    const val DESCRIPTION_RESPONSE = "description"
    const val ASSET_RESPONSE = "asset"
    const val ASSET_BALANCES_RESPONSE = "assetBalances"
    const val UNCONFIRMED_ASSET_BALANCES_RESPONSE = "unconfirmedAssetBalances"
    const val ACCOUNT_RESPONSE = "account"
    const val REWARD_RECIPIENT_RESPONSE = "rewardRecipient"
    const val BLOCK_RESPONSE = "block"
    const val HEIGHT_RESPONSE = "height"
    const val GENERATOR_RESPONSE = "generator"
    const val GENERATOR_PUBLIC_KEY_RESPONSE = "generatorPublicKey"
    const val DATA_RESPONSE = "data"
    const val NONCE_RESPONSE = "nonce"
    const val SCOOP_NUM_RESPONSE = "scoopNum"
    const val TIMESTAMP_RESPONSE = "timestamp"
    const val NUMBER_OF_TRANSACTIONS_RESPONSE = "numberOfTransactions"
    const val TOTAL_AMOUNT_PLANCK_RESPONSE = "totalAmountNQT"
    const val TOTAL_FEE_PLANCK_RESPONSE = "totalFeeNQT"
    const val BLOCK_REWARD_RESPONSE = "blockReward"
    const val PAYLOAD_LENGTH_RESPONSE = "payloadLength"
    const val VERSION_RESPONSE = "version"
    const val BASE_TARGET_RESPONSE = "baseTarget"
    const val PREVIOUS_BLOCK_RESPONSE = "previousBlock"
    const val NEXT_BLOCK_RESPONSE = "nextBlock"
    const val PAYLOAD_HASH_RESPONSE = "payloadHash"
    const val GENERATION_SIGNATURE_RESPONSE = "generationSignature"
    const val PREVIOUS_BLOCK_HASH_RESPONSE = "previousBlockHash"
    const val BLOCK_SIGNATURE_RESPONSE = "blockSignature"
    const val TRANSACTIONS_RESPONSE = "transactions"
    const val ASSETS_RESPONSE = "assets"
    const val OPEN_ORDERS_RESPONSE = "openOrders"
    const val TYPE_RESPONSE = "type"
    const val SUBTYPE_RESPONSE = "subtype"
    const val ORDER_RESPONSE = "order"
    const val QUANTITY_QNT_RESPONSE = "quantityQNT"
    const val UNCONFIRMED_QUANTITY_RESPONSE = "unconfirmedQuantityQNT"
    const val PRICE_PLANCK_RESPONSE = "priceNQT"
    const val DECIMALS_RESPONSE = "decimals"
    const val NUMBER_OF_TRADES_RESPONSE = "numberOfTrades"
    const val NUMBER_OF_TRANSFERS_RESPONSE = "numberOfTransfers"
    const val NUMBER_OF_ACCOUNTS_RESPONSE = "numberOfAccounts"
    const val GOODS_RESPONSE = "goods"
    const val QUANTITY_RESPONSE = "quantity"
    const val SELLER_RESPONSE = "seller"
    const val TAGS_RESPONSE = "tags"
    const val DELISTED_RESPONSE = "delisted"
    const val ASSET_IDS_RESPONSE = "assetIds"
    const val PURCHASE_RESPONSE = "purchase"
    const val BUYER_RESPONSE = "buyer"
    const val DELIVERY_DEADLINE_TIMESTAMP_RESPONSE = "deliveryDeadlineTimestamp"
    const val PENDING_RESPONSE = "pending"
    const val NOTE_RESPONSE = "note"
    const val GOODS_DATA_RESPONSE = "goodsData"
    const val GOODS_IS_TEXT_RESPONSE = "goodsIsText"
    const val FEEDBACK_NOTES_RESPONSE = "feedbackNotes"
    const val PUBLIC_FEEDBACKS_RESPONSE = "publicFeedbacks"
    const val REFUND_NOTE_RESPONSE = "refundNote"
    const val DISCOUNT_PLANCK_RESPONSE = "discountNQT"
    const val REFUND_PLANCK_RESPONSE = "refundNQT"
    const val PURCHASES_RESPONSE = "purchases"
    const val ALIAS_NAME_RESPONSE = "aliasName"
    const val ALIAS_URI_RESPONSE = "aliasURI"
    const val ALIAS_RESPONSE = "alias"
    const val ID_RESPONSE = "id"
    const val SENDER_RESPONSE = "sender"
    const val SENDER_RS_RESPONSE = "senderRS"
    const val RECIPIENT_RESPONSE = "recipient"
    const val RECIPIENT_RS_RESPONSE = "recipientRS"
    const val AMOUNT_PLANCK_RESPONSE = "amountNQT"
    const val FEE_PLANCK_RESPONSE = "feeNQT"
    const val REQUIRED_SIGNERS_RESPONSE = "requiredSigners"
    const val DEADLINE_RESPONSE = "deadline"
    const val DEADLINE_ACTION_RESPONSE = "deadlineAction"
    const val ID_RS_RESPONSE = "idRS"
    const val DECISION_RESPONSE = "decision"
    const val SIGNERS_RESPONSE = "signers"
    const val ATS_RESPONSE = "ats"
    const val AT_IDS_RESPONSE = "atIds"
    const val TRADES_RESPONSE = "trades"
    const val ASK_ORDER_RESPONSE = "askOrder"
    const val BID_ORDER_RESPONSE = "bidOrder"
    const val ASK_ORDER_HEIGHT_RESPONSE = "askOrderHeight"
    const val BID_ORDER_HEIGHT_RESPONSE = ASK_ORDER_HEIGHT_RESPONSE
    const val TRADE_TYPE_RESPONSE = "tradeType"
    const val ASSET_TRANSFER_RESPONSE = "assetTransfer"
    const val CONFIRMATIONS_RESPONSE = "confirmations"
    const val BLOCK_TIMESTAMP_RESPONSE = "blockTimestamp"
    const val SENDER_PUBLIC_KEY_RESPONSE = "senderPublicKey"
    const val REFERENCED_TRANSACTION_FULL_HASH_RESPONSE = "referencedTransactionFullHash"
    const val ATTACHMENT_RESPONSE = "attachment"
    const val EC_BLOCK_ID_RESPONSE = "ecBlockId"
    const val EC_BLOCK_HEIGHT_RESPONSE = "ecBlockHeight"
    const val FREQUENCY_RESPONSE = "frequency"
    const val TIME_NEXT_RESPONSE = "timeNext"
    const val BLOCK_IDS_RESPONSE = "blockIds"
    const val BID_ORDERS_RESPONSE = "bidOrders"
    const val TIME_RESPONSE = "time"
    const val ALIASES_RESPONSE = "aliases"
    const val BID_ORDER_IDS_RESPONSE = "bidOrderIds"
    const val TRANSFERS_RESPONSE = "transfers"
    const val LESSORS_RESPONSE = "lessors"
    const val DELTA_QUANTITY_RESPONSE = "deltaQuantity"
    const val URI_RESPONSE = "uri"
    const val PERIOD_RESPONSE = "period"
    const val COMMENT_RESPONSE = "comment"
    const val GOODS_NONCE_RESPONSE = "goodsNonce"
    const val ESCROW_ID_RESPONSE = "escrowId"
    const val SUBSCRIPTION_ID_RESPONSE = "subscriptionId"
    const val CREATION_BYTES_RESPONSE = "creationBytes"
    const val LAST_UNCONFIRMED_TRANSACTION_TIMESTAMP_RESPONSE = "lastUnconfirmedTransactionTimestamp"
    const val UNCONFIRMED_TRANSACTIONS_RESPONSE = "unconfirmedTransactions"
    const val UNCONFIRMED_TRANSACTIONS_IDS_RESPONSE = "unconfirmedTransactionIds"
    const val CHEAP_FEE_RESPONSE = "cheap"
    const val STANDARD_FEE_RESPONSE = "standard"
    const val PRIORITY_FEE_RESPONSE = "priority"
}
