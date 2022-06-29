package com.eme22.applicacioncomida.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eme22.applicacioncomida.data.api.WebApiAdapter;
import com.eme22.applicacioncomida.data.api.WebApiService;
import com.eme22.applicacioncomida.data.model.Cart;
import com.eme22.applicacioncomida.data.model.CartItem;
import com.eme22.applicacioncomida.data.model.Item;
import com.eme22.applicacioncomida.data.model.Promo;
import com.eme22.applicacioncomida.data.model.User;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends ViewModel {

    private final WebApiService service = WebApiAdapter.getApiService();

    private final MutableLiveData<Cart> cart = new MutableLiveData<>();

    LiveData<Cart> getCart() {
        return cart;
    }

    private final MutableLiveData<Boolean> hasItems = new MutableLiveData<>(false);

    LiveData<Boolean> isHasItems() {
        return hasItems;
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    private User user;

    public void getCurrentCart(User user) {

        executor.execute(() -> {

            try {
                this.user = user;
                Response<Cart> cartResponse = service.getCurrentCart(Math.toIntExact(this.user.getId())).execute();
                if (cartResponse.isSuccessful()) {
                    Cart cart = cartResponse.body();
                    if (cart != null) {
                        ArrayList<CartItem> cartItems = cart.getCartItems();

                        if (cartItems.size() > 0)
                            this.hasItems.postValue(true);

                        for (int i = 0; i < cartItems.size() ; i++) {
                            CartItem cartItem = cartItems.get(i);
                            Response<Item> itemResponse = service.getItem(Math.toIntExact(cartItem.getId())).execute();
                            if (itemResponse.isSuccessful()) {

                                Item item = itemResponse.body();

                                if (item != null) {
                                    if (item.getPromoId() != null) {
                                        Response<Promo> promoResponse = service.getPromo(Math.toIntExact(item.getPromoId())).execute();
                                        if (promoResponse.isSuccessful())
                                            item.setPromo(promoResponse.body());
                                    }
                                    cartItem.setItem(item);
                                    //cartItems.set(i, cartItem);
                                }

                            }
                        }
                        //cart.setCartItems(cartItems);
                        this.cart.postValue(cart);
                    }
                    else {
                        this.cart.postValue(createNewCart(null));
                        this.hasItems.postValue(false);
                    }
                }

                else  {
                    cart.postValue(createNewCart(null));
                    this.hasItems.postValue(false);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Item retrieveItem(Integer id) throws IOException {
        Response<Item> itemdata = service.getItem(id).execute();

        if (itemdata.isSuccessful()) {
            return itemdata.body();
        }

        return null;

    }

    public void addItem(CartItem cartItem, User user) {
        Cart cart1 = cart.getValue();
        if (cart1 != null) {
            ArrayList<CartItem> items;
            items = cart1.getCartItems();
            items.add(cartItem);
            cart1.setCartItems(items);
            cart.setValue(cart1);

        }
        else {
            cart.setValue(createNewCart(cartItem));
        }
        hasItems.setValue(true);
    }


    public void deleteCartItem(CartItem cartItem) {
        Cart cart1 = cart.getValue();

        ArrayList<CartItem> items;
        if (cart1 != null) {
            items = cart1.getCartItems();
            items.remove(cartItem);
            cart1.setCartItems(items);
            cart.setValue(cart1);
        }
    }

    public Promo retrievePromo(int id) throws IOException {

        Response<Promo> itemdata = service.getPromo(id).execute();

        if (itemdata.isSuccessful()) {
            return itemdata.body();
        }

        return null;

    }

    public void submitCart() {

        finishCart();

        service.uploadCart(cart.getValue()).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    cart.setValue(createNewCart(null));
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {

            }
        });
    }

    private void finishCart() {

        Cart cart = this.getCart().getValue();
        if (cart != null) {
            cart.setFinished(true);
            this.cart.setValue(cart);
        }


    }

    private Cart createNewCart (@Nullable CartItem cartItem) {
        return new Cart(0, this.user, this.user.getId(), cartItem != null ? new ArrayList<>(Collections.singletonList(cartItem)) : new ArrayList<>(), false, null);
    }
}