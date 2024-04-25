package com.github.parking.smartparking.home.data.repository


import com.github.parking.smartparking.auth.core.util.Resource
import com.github.parking.smartparking.auth.core.util.UiText
import com.github.parking.smartparking.home.data.remote.MpesaPay
import com.github.parking.smartparking.home.domain.model.STKPushResponse
import com.github.parking.smartparking.home.domain.PaymentRepository
import com.github.parking.smartparking.home.domain.model.ParkingProvider
import com.github.parking.smartparking.home.domain.model.STKPushQuery
import com.github.parking.smartparking.home.domain.model.STKPushQueryResponse
import com.github.parking.smartparking.home.domain.model.Slot
import com.github.parking.smartparking.home.domain.model.TransactionDetails
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.auth.User

class PaymentRepositoryImpl @Inject constructor() : PaymentRepository {
    private val api = MpesaPay
    override  fun sendSTKPush(request: TransactionDetails): Resource<STKPushResponse> {
        try {
            api.sendPush(request).let {
               return Resource.Success(it)
            }
        }catch (e:Exception){
            return Resource.Error(UiText.DynamicString("An error occurred"))
            }
//        return Resource.Error(UiText.DynamicString("An error occurred"))
    }

    override fun querySTKPush(request: STKPushQuery): Resource<STKPushQueryResponse> {
        return Resource.Error(UiText.DynamicString("Not yet implemented"))
    }







    override suspend fun storePaymentAndUpdates(user: FirebaseUser, payment: Payment, provider: ParkingProvider, slot: Slot): Task<Void> {
        val db = FirebaseFirestore.getInstance()

        // Start a new batch
        val batch: WriteBatch = db.batch()

        // Create a document reference for the user's payment history
        val paymentRef = db.collection("users").document(user.uid).collection("payments").document(payment.id!!)

        // Add the payment details to the batch
        batch.set(paymentRef, payment.toMap())

        // Create a document reference for the provider
        val providerRef = db.collection("providers").document(provider.id)

        // Update the necessary fields in the batch
        batch.update(providerRef, "availableSlots", provider.availableSlots - 1)

        // Create a document reference for the slot
        val slotRef = providerRef.collection("slots").document(slot.id)

        // Update the slot's occupancy status
        batch.update(slotRef, "isOccupied", true)

        // Commit the batch write operation
        return batch.commit()
    }
}

data class Payment(
    val MerchantRequestID: String? = null,
    val id: String? = null, //checkoutRequestID
    val ResponseCode: String? = null,
    val ResponseDescription: String? = null,
    val CustomerMessage: String? = null,
    val amount: Int? = null,
    val parkingtime: Int? = null,
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "MerchantRequestID" to MerchantRequestID,
            "id" to id,
            "ResponseCode" to ResponseCode,
            "ResponseDescription" to ResponseDescription,
            "CustomerMessage" to CustomerMessage,
            "amount" to amount,
            "parkingtime" to parkingtime
        )
    }
    constructor(): this(null, null, null, null, null, null, null)
}