package us.xingkong.flyu;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

/**
 * @作者: Xuer
 * @包名: us.xingkong.flyu
 * @类名: PicturesAdapter
 * @创建时间: 2018/5/10 13:02
 * @最后修改于:
 * @版本: 1.0
 * @描述:
 * @更新日志:
 */
public class PhotosAdapterT extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = 1;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<PhotoBean> mList;
    private View mFooterView;
    private onItemClickListener mListener;
    private onAddClickListener listener;

    public PhotosAdapterT(Context context, List<PhotoBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public interface onAddClickListener {
        void onAddClick();
    }

    public void setOnAddClickListener(onAddClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    public void delete(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mFooterView != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            setItemWidth(mFooterView, parent);
            return new AddHolder(mFooterView);
        }
        View view = mInflater.inflate(R.layout.activity_photo_item, parent, false);
        setItemWidth(view, parent);
        return new ItemHolder(view);
    }

    private void setItemWidth(View view, ViewGroup parent) {
        view.getLayoutParams().width = parent.getWidth() / 3;
        view.getLayoutParams().height = parent.getWidth() / 3;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_NORMAL:
                //setItem((ItemHolder) holder, mList);
                if (mList != null) {
                    Uri uri = Uri.parse(mList.get(position).getUri());
                    Glide.with(mContext)
                            .load(uri)
                            .thumbnail(0.5f)
                            .into(((ItemHolder) holder).photoImage);
                }
                if (mListener != null) {
                    ((ItemHolder) holder).photoItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onItemClick(position);
                        }
                    });
                }
                ((ItemHolder) holder).photoItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
                ((ItemHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(position);
                    }
                });
                break;
            case TYPE_FOOTER:

                if (mList == null || mList.size() < 3) {
                    ((AddHolder) holder).photoAdd.setEnabled(true);
                } else {
                    ((AddHolder) holder).photoAdd.setEnabled(false);
                }

                if (listener != null) {
                    ((AddHolder) holder).photoAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onAddClick();
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    private void setItem(ItemHolder holder, List<PhotoBean> list) {
        final int position = holder.getLayoutPosition();
        if (list != null) {
            Uri uri = Uri.parse(mList.get(position).getUri());
            Glide.with(mContext)
                    .load(uri)
                    .thumbnail(0.5f)
                    .into(holder.photoImage);
        }
        if (mListener != null) {
            holder.photoItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });
        }
        holder.photoItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mFooterView == null) {
            return mList.size();
        } else if (mList != null) {
            return mList.size() + 1;
        } else return 1;
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }


    class AddHolder extends RecyclerView.ViewHolder {

        CardView photoAdd;

        AddHolder(View itemView) {
            super(itemView);
            photoAdd = itemView.findViewById(R.id.photo_add);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        CardView photoItem;
        ImageView photoImage, delete;

        ItemHolder(View itemView) {
            super(itemView);
            photoItem = itemView.findViewById(R.id.photo_item);
            photoImage = itemView.findViewById(R.id.photo_image);
            delete = itemView.findViewById(R.id.delete_item);
        }
    }
}