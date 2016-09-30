package io.cadi.souklou.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.cadi.souklou.R;
import io.cadi.souklou.activity.PreviewChildActivity;
import io.cadi.souklou.database.DbConstant;
import io.cadi.souklou.database.ListenerDb;
import io.cadi.souklou.models.Children;

/**
 * Created by arcadius on 09/08/16.
 */
public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ViewHolder> {

    private Context context;
    private List<Children> listChildren;

    public ChildrenAdapter(Context context, List<Children> listChildren) {
        this.context = context;
        this.listChildren = listChildren;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Children children = listChildren.get(position);

        holder.txtName.setText(children.getFirstName());
        holder.txtSchool.setText(children.getSchool());
        getImage(children.getImageTitle(), new ListenerDb() {
            @Override
            public void onSuccess(Object o) {
                File f = (File) o;
                Drawable d = Drawable.createFromPath(f.getPath());
                holder.imgPreviewChild.setImageDrawable(d);
            }

            @Override
            public void onFailed(Object o) {
                Log.e("error download","failed");
            }
        });

        holder.btnChildRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PreviewChildActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listChildren.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnChildRow;
        CircleImageView imgPreviewChild;
        TextView txtName,txtSchool;
        ImageView imgStart;
        public ViewHolder(View itemView) {
            super(itemView);
            btnChildRow = (Button) itemView.findViewById(R.id.btnChildRow);
            imgPreviewChild = (CircleImageView) itemView.findViewById(R.id.imgPreviewChild);
            imgStart = (ImageView) itemView.findViewById(R.id.imgStar);
            txtName = (TextView) itemView.findViewById(R.id.txtNameChild);
            txtSchool = (TextView) itemView.findViewById(R.id.txtSchoolChild);
        }
    }

    private void getImage(String imageTitle, final ListenerDb callback) {
        StorageReference isimageRef = DbConstant.FIREBASE_STORAGE_CHILD.child(imageTitle);
        try {
            final File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageTitle);
            if (file.isFile()) {
                callback.onSuccess(file);
                file.delete();
            }
            file.createNewFile();
            //localFile = File.createTempFile("images", "jpg");
            isimageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    callback.onSuccess(file);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    callback.onFailed(exception);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailed(e);
        }


    }
}
