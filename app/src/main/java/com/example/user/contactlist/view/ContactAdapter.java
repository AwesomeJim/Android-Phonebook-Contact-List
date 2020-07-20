package com.example.user.contactlist.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.contactlist.R;
import com.example.user.contactlist.databinding.ItemContactBinding;
import com.example.user.contactlist.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contacts;
    private ItemContactBinding binding;
    private Context context;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    private List<Contact> filtered_icontacts = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();


    private OnItemClickListener mOnItemClickListener;

    /**
     * The interface On item click listener.
     */
    public interface OnItemClickListener {
        /**
         * On item click.
         *
         * @param view     the view
         * @param obj      the obj
         * @param position the position
         */
        void onItemClick(View view, Contact obj, int position);
    }

    /**
     * Sets on item click listener.
     *
     * @param mItemClickListener the m item click listener
     */
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }


    ContactAdapter(Context context) {
        this.context = context;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        this.filtered_icontacts = contacts;
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
        contactViewHolder.onBind(filtered_icontacts.get(i));
        // Here you apply the animation when the view is bound
        setAnimation(contactViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        // return contacts != null ? contacts.size() : 0;
        return filtered_icontacts.size();
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
            binding.selectedContactLayout.setOnClickListener(view ->
                    mOnItemClickListener.onItemClick(view, contact, getAdapterPosition()));
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
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
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


    /**
     * Gets filter.
     *
     * @return the filter
     */
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Contact> list = contacts;
            final List<Contact> result_list = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).getName();
                if (str_title.toLowerCase().contains(query)) {
                    result_list.add(list.get(i));
                }
            }
            results.values = result_list;
            results.count = result_list.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered_icontacts = (List<Contact>) results.values;
            notifyDataSetChanged();
        }
    }
}
