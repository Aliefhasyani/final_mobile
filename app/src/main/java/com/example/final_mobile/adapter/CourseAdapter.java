package com.example.final_mobile.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_mobile.R;
import com.example.final_mobile.model.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private List<Course> courses;
    private OnItemClickListener listener;
    private OnLoadMoreListener loadMoreListener;

    public interface OnItemClickListener {
        void onItemClick(Course course);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public CourseAdapter(List<Course> courses, OnItemClickListener listener, OnLoadMoreListener loadMoreListener) {
        this.courses = courses;
        this.listener = listener;
        this.loadMoreListener = loadMoreListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_course, parent, false);
            return new CourseViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_load_more, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CourseViewHolder) {
            Course course = courses.get(position);
            CourseViewHolder viewHolder = (CourseViewHolder) holder;

            viewHolder.tvTitle.setText(course.getTitle());
            viewHolder.tvPrice.setText(course.getPrice());
            viewHolder.tvCategory.setText(course.getCategory());

            String imageUrl = course.getImage();
            Log.d("CourseAdapter", "Loading image from URL: " + imageUrl);

            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image) // Make sure you have this drawable
                    .error(R.drawable.error_image) // Make sure you have this drawable
                    .centerCrop()
                    .into(viewHolder.imgCourse);

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(course);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.btnLoadMore.setOnClickListener(v -> {
                if (loadMoreListener != null) {
                    loadMoreListener.onLoadMore();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return loadMoreListener != null ? courses.size() + 1 : courses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == courses.size() ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void updateData(List<Course> newCourses) {
        this.courses = newCourses;
        notifyDataSetChanged();
    }

    public void addCourses(List<Course> moreCourses) {
        int startPos = courses.size();
        courses.addAll(moreCourses);
        notifyItemRangeInserted(startPos, moreCourses.size());
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCourse;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvCategory;

        CourseViewHolder(View view) {
            super(view);
            imgCourse = view.findViewById(R.id.imgCourse);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvCategory = view.findViewById(R.id.tvCategory);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        Button btnLoadMore;

        LoadingViewHolder(View view) {
            super(view);
            btnLoadMore = view.findViewById(R.id.btnLoadMore);
        }
    }
}