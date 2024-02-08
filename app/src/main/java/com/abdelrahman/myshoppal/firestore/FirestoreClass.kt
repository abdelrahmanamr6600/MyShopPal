package com.abdelrahman.myshoppal.firestore
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.abdelrahman.myshoppal.models.*
import com.abdelrahman.myshoppal.ui.ui.ui.fragments.ProductsFragment
import com.abdelrahman.myshoppal.ui.activities.*
import com.abdelrahman.myshoppal.ui.fragments.SoldProductsFragment
import com.abdelrahman.myshoppal.ui.ui.ui.dashboard.DashboardFragment
import com.abdelrahman.myshoppal.ui.ui.ui.fragments.OrdersFragment
import com.abdelrahman.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {
    companion object{

    }

    private val mFireStore = FirebaseFirestore.getInstance()


    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection(Constants.Users)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
         .addOnSuccessListener {

             // Here call a function of base activity for transferring the result to it.
             activity.userRegistrationSuccess()
         }
         .addOnFailureListener { e ->
             activity.hideDialog()
             Log.e(
                 activity.javaClass.simpleName,
                 "Error while registering the user.",
                 e
             )
         }
 }

    fun getCurrentUser():String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = " "
        if(currentUser != null)
        {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
    fun getCurrentUserDetails(activity : Activity)
    {
        mFireStore.collection(Constants.Users)
            .document(getCurrentUser())
            .get().addOnSuccessListener {
                Log.i(activity.javaClass.simpleName,it.toString())
                val user = it.toObject(User::class.java)!!

               val  SharedPreferences = activity.getSharedPreferences(Constants.myShopPreferences,Context.MODE_PRIVATE)
                val editor:SharedPreferences.Editor = SharedPreferences.edit()
                editor.putString(Constants.Logged_In_Username,"${user.firstName} ${user.lastName}")
                editor.apply()
                when (activity)
                {
                    is LoginActivity ->{
                        activity.userLoggedInSuccess(user)
                    }

                    is SettingsActivity ->{
                      activity.userDetailsSuccess(user)
                    }

                }
            }
    }

    fun updateUserProfile(activity: Activity,userHashMap:HashMap<String,Any>){

        mFireStore.collection(Constants.Users)
            .document(getCurrentUser())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity ->{
                        activity.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener {
                when(activity){
                    is UserProfileActivity ->{
                        activity.hideDialog()
                    }
                }

            }

    }


    fun uploadImageToCloudStorage(activity:Activity,imageFileUri:Uri?,imageType:String){
        val sRef:StorageReference=FirebaseStorage.getInstance().reference.child(
            imageType+ System.currentTimeMillis() +"." +Constants.getFileExtensions(activity,imageFileUri)
        )
        sRef.putFile(imageFileUri!!).addOnSuccessListener {taskSnapShot ->
            Log.e("FireBase Image Url",
                taskSnapShot.metadata!!.reference!!.downloadUrl.toString())
            taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { Uri ->
                Log.d("Image Url",Uri.toString())
          when(activity){
              is UserProfileActivity ->{
                  activity.hideDialog()
                  activity.imageUploadSuccess(Uri.toString())
                  Log.d("TAG",Uri.toString())
              }
              is AddProductActivity ->{
                  activity.imageUploadSuccess(Uri.toString())
              }
          }
            }
                .addOnFailureListener { Exception ->
                    when(activity){
                        is UserProfileActivity ->{
                            activity.hideDialog()
                        }
                        is AddProductActivity ->{
                           activity.hideDialog()
                        }
                    }
                    Log.e(activity.javaClass.simpleName,Exception.message,Exception)
                }
        }
    }

    fun addProduct(activity: AddProductActivity, productInfo:Product){
        mFireStore.collection(Constants.Products)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productAddedSuccess()
            }
            .addOnFailureListener {
                activity.hideDialog()
                Toast.makeText(activity.applicationContext,it.message.toString(),Toast.LENGTH_LONG).show()
            }
    }

    fun getProductsList(fragment:ProductsFragment){
        mFireStore.collection(Constants.Products)
            .whereEqualTo(Constants.USER_ID,getCurrentUser())
            .get().addOnSuccessListener  { document ->
                val productsList:ArrayList<Product> = ArrayList()
                for (i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }
                when(fragment)
                {
                    is ProductsFragment ->
                    {
                        fragment.successProductsFromFireStore(productsList)
                    }
                }
            }
    }

    fun getDashboardItems(fragment: DashboardFragment){
        mFireStore.collection(Constants.Products)
                .get()
                .addOnSuccessListener {document ->
                    val productsList:ArrayList<Product> = ArrayList()
                    for (i in document.documents )
                    {
                        val product = i.toObject(Product::class.java)
                        product!!.product_id = i.id
                        productsList.add(product)
                    }
                    when(fragment)
                    {
                        is DashboardFragment ->
                        {
                            fragment.successProductsFromFireStore(productsList)
                        }
                    }

                }.addOnFailureListener{
                fragment.hideDialog()
            }


    }

    fun deleteProduct(fragment:ProductsFragment,product_id:String){
        mFireStore.collection(Constants.Products).document(product_id)
                .delete()
                .addOnSuccessListener {
                    fragment.productDeleteSuccess()

                }.addOnFailureListener {
                    Toast.makeText(fragment.requireContext(),"Error",Toast.LENGTH_LONG).show()

                }
    }

    fun getProductDetails(activity: ProductDetailsActivity, product_id:String)
    {
        mFireStore.collection(Constants.Products)
            .document(product_id)
            .get()
            .addOnSuccessListener { document ->
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    activity.successProductDetails(product)
                }
            }
    }

    fun addProductToCart(activity: ProductDetailsActivity, cart:CartItem)
    {
        mFireStore.collection(Constants.CART_ITEMS)
            .document()
            .set(cart, SetOptions.merge()).addOnSuccessListener {
                activity.addToCartSuccess()

            }.addOnFailureListener {
                activity.hideDialog()
                Toast.makeText(activity.applicationContext,"Error While adding Product to your Cart",Toast.LENGTH_LONG).show()

            }
    }
   fun updateMyCart(context: Context,cart_id:String,itemHashMap: HashMap<String,Any>){
  mFireStore.collection(Constants.CART_ITEMS)
      .document(cart_id)
      .update(itemHashMap)
      .addOnSuccessListener {
          when(context){
              is CartListActivity ->{
                   context.itemUpdateSuccess()


              }

          }
      }
      .addOnFailureListener {
          when(context){
              is CartListActivity ->{
                  context.hideDialog()
              }
          }

      }
   }
    fun checkIfItemExistInCart(activity: ProductDetailsActivity, product_id:String){
        mFireStore.collection(Constants.CART_ITEMS)
                .whereEqualTo(Constants.USER_ID,getCurrentUser())
                .whereEqualTo(Constants.PRODUCT_ID,product_id)
                .get()
                .addOnSuccessListener {
                  if (it.documents.size>0){
                      activity.productExistInCart()
                  }
                    else
                        activity.hideDialog()

                }
                .addOnFailureListener {
                    activity.hideDialog()
                }
    }

    fun getCartList(activity:Activity){
        mFireStore.collection(Constants.CART_ITEMS)
                .whereEqualTo(Constants.USER_ID,getCurrentUser())
                .get()
                .addOnSuccessListener { document ->

                    val list :ArrayList<CartItem> = ArrayList()

                    for (i in document.documents) {

                        val cartItem = i.toObject(CartItem::class.java)!!
                        cartItem.id= i.id
                        list.add(cartItem)
                    }
                    when(activity)
                    {
                        is CartListActivity ->{
                            activity.successCartItemList(list)
                        }
                        is CheckoutActivity ->{
                            activity.successCartItemsList(list)
                        }
                    }
                }
                .addOnFailureListener {
                    when(activity)
                    {
                        is CartListActivity ->{
                            activity.hideDialog()
                            Toast.makeText(activity.applicationContext,"error",Toast.LENGTH_LONG).show()
                        }
                        is CheckoutActivity ->{
                            activity.hideDialog()
                            Toast.makeText(activity.applicationContext,"error",Toast.LENGTH_LONG).show()
                        }
                    }
                }
    }

    fun getAllProductsList(activity: Activity) {

        mFireStore.collection(Constants.Products)
                .get()
                .addOnSuccessListener { document ->
                    Log.e("Products List", document.documents.toString())
                    val productsList: ArrayList<Product> = ArrayList()
                    for (i in document.documents) {
                        val product = i.toObject(Product::class.java)
                        product!!.product_id = i.id
                        productsList.add(product)
                    }
                      when(activity){
                          is CartListActivity ->{
                              activity.successProductsListFromFireStore(productsList)
                          }
                          is CheckoutActivity ->{
                              activity.successProductsListFromFireStore(productsList)
                          }
                      }
                }
                .addOnFailureListener { e ->
                    when(activity){
                        is CartListActivity -> {
                            activity.hideDialog()
                        }
                        is CheckoutActivity -> {
                            activity.hideDialog()
                        }
                        }
                    Log.e("Get Product List", "Error while getting all product list.", e)
                }
    }



    fun deleteCartItem(activity: CartListActivity, cart_id:String){
        mFireStore.collection(Constants.CART_ITEMS)
                .document(cart_id)
                .delete()
                .addOnSuccessListener {
                    activity.productDeleteSuccess()

                }.addOnFailureListener {
                    Toast.makeText(activity.applicationContext,"Error",Toast.LENGTH_LONG).show()
                }
    }

    fun addAddress(activity:AddEditAddressActivity , addressInfo:Address){
       mFireStore.collection(Constants.ADDRESSES)
               .document()
               .set(addressInfo, SetOptions.merge())
               .addOnSuccessListener {
                   activity.addUpdateAddressSuccessful()
               }.addOnFailureListener {
               }
    }

    fun getAddresses(activity:AddressListActivity)
    {
        mFireStore.collection(Constants.ADDRESSES)
                .whereEqualTo(Constants.USER_ID,getCurrentUser())
                .get()
                .addOnSuccessListener {
                var list :ArrayList<Address>  = ArrayList();
                    for (i in it.documents)
                    {
                        val address = i.toObject(Address::class.java)
                        if (address != null) {
                            address.id = i.id
                        }

                        if (address != null) {
                            list.add(address)
                        }

                    }
                    activity.getAddressesSuccessful(list)

                }
    }
    fun updateAddress(activity: AddEditAddressActivity,addressInfo:Address,addressId:String){
        mFireStore.collection(Constants.ADDRESSES)
                .document(addressId)
                .set(addressInfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.addUpdateAddressSuccessful()
                }.addOnFailureListener {
                  activity.hideDialog()
                }
    }
    fun deleteAddress(activity: AddressListActivity,addressId:String){
        mFireStore.collection(Constants.ADDRESSES)
                .document(addressId)
                .delete().addOnSuccessListener {
                    activity.deleteAddressSuccessful()

                }
                .addOnFailureListener {
                   Toast.makeText(activity.applicationContext,"Error",Toast
                           .LENGTH_LONG).show()
                }
    }
    fun placeOrder(activity: CheckoutActivity,order: Order){
        mFireStore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                 activity.orderPlacedSuccessful()
            }.addOnFailureListener {
                activity.hideDialog()
                Toast.makeText(activity.applicationContext,"Error",Toast.LENGTH_LONG).show()
            }
    }

    fun updateAllDetails(activity: CheckoutActivity,cartList:ArrayList<CartItem>,order:Order){
        val writeBatch = mFireStore.batch()

        // Prepare the sold product details

        for (cart in cartList) {

            val soldProduct = SoldProduct(
                cart.seller_id,
                cart.title,
                cart.price,
                cart.cart_Quantity,
                cart.image,
                order.title,
                order.order_datetime,
                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address
            )

            val documentReference = mFireStore.collection(Constants.SOLD_PRODUCTS)
                .document(cart.productId)
            writeBatch.set(documentReference, soldProduct)
        }

        // Here we will update the product stock in the products collection based to cart quantity.

        for(cartItem in cartList){
            val productHashMap = HashMap<String,Any>()
            productHashMap[Constants.STOCK_QUANTITY]=
                (cartItem.stock_Quantity.toInt() - cartItem.cart_Quantity.toInt()).toString()
            val documentRefrence = mFireStore.collection(Constants.Products)
                .document(cartItem.productId)
            writeBatch.update(documentRefrence,productHashMap)
        }

        for (cartItem in cartList)
        {
            val documentRefrence = mFireStore.collection(Constants.CART_ITEMS)
                .document(cartItem.id)
            writeBatch.delete(documentRefrence)
        }
            writeBatch.commit().addOnSuccessListener {
                activity.allDetailsUpdatedSuccessfully()
            }
                .addOnFailureListener {
                    activity.hideDialog()
                }
    }


    fun getMyOrdersList(fragment:OrdersFragment){
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID,getCurrentUser())
            .get().addOnSuccessListener { document->
                var list :ArrayList<Order>  = ArrayList();
                for (i in document.documents){
                    val orderItem = i.toObject(Order::class.java)
                    if (orderItem != null) {
                        orderItem.id = i.id
                    }
                    if (orderItem != null) {
                        list.add(orderItem)
                    }
                    fragment.populateOrdersListinUi(list)

                }

            }.addOnFailureListener {
                fragment.hideDialog()
            }
    }

    fun getSoldProductsList(fragment: SoldProductsFragment) {
        // The collection name for SOLD PRODUCTS
        mFireStore.collection(Constants.SOLD_PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUser())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                Log.e(fragment.javaClass.simpleName, document.documents.toString())


                val list: ArrayList<SoldProduct> = ArrayList()

                for (i in document.documents) {

                    val soldProduct = i.toObject(SoldProduct::class.java)!!
                    soldProduct.id = i.id

                    list.add(soldProduct)
                }

                fragment.successSoldProductsList(list)
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error.
                fragment.hideDialog()

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting the list of sold products.",
                    e
                )
            }
    }

}