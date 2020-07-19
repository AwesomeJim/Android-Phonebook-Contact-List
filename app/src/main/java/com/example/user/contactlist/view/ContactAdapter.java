package com.example.user.contactlist.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.contactlist.R;
import com.example.user.contactlist.databinding.ItemContactBinding;
import com.example.user.contactlist.model.Contact;

import java.util.List;
import java.util.Random;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contacts;
    private ItemContactBinding binding;
    private Context context;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    ContactAdapter(Context context) {
        this.context = context;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_contact,
                viewGroup, false);
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, int i) {
        contactViewHolder.onBind(contacts.get(i));
        // Here you apply the animation when the view is bound
        setAnimation(contactViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {

        private final ItemContactBinding binding;

        ContactViewHolder(ItemContactBinding itemContactBinding) {
            super(itemContactBinding.getRoot());
            this.binding = itemContactBinding;
        }

        void onBind(Contact contact) {
            //binding.setVariable(BR.contact, contact);
            // binding.setContact(contact);
            binding.contactName.setText(contact.getName());
            binding.contactNumber.setText(contact.getPhoneNumber());
            if (contact.getPhotoUri() != null) {
                binding.drawableTextView.setVisibility(View.GONE);
                binding.contactPhoto.setVisibility(View.VISIBLE);
                Contact.loadImage(binding.contactPhoto, contact.getPhotoUri());
            } else {
                binding.contactPhoto.setVisibility(View.GONE);
                binding.drawableTextView.setVisibility(View.VISIBLE);
                GradientDrawable gradientDrawable = (GradientDrawable) binding.drawableTextView.getBackground();
                gradientDrawable.setColor(getRandomColor());
                String serviceSubString = (contact.getName().substring(0, 2));
                binding.drawableTextView.setText(serviceSubString.toUpperCase());
            }
            binding.executePendingBindings();
        }
    }

/*        *
     * Here is the key method to apply the animation*/

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    /**
     * @return a random color which is used a background by
     * services initial letters
     */
    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
