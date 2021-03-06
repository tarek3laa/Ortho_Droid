package com.example.shaimaaderbaz.orthoclinic.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.shaimaaderbaz.orthoclinic.R;
import com.example.shaimaaderbaz.orthoclinic.adapters.ImageItemAdapter;
import com.example.shaimaaderbaz.orthoclinic.adapters.VedioItemAdapter;
import com.example.shaimaaderbaz.orthoclinic.fragments.PersonalFragment;
import com.example.shaimaaderbaz.orthoclinic.models.MediaItem;
import com.example.shaimaaderbaz.orthoclinic.models.OperationsItem;
import com.example.shaimaaderbaz.orthoclinic.models.PatientProfile;
import com.example.shaimaaderbaz.orthoclinic.presenter.EditOperationPresenterImp;
import com.example.shaimaaderbaz.orthoclinic.views.EditOperationsView;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditOperationActivity extends AppCompatActivity implements EditOperationsView,
        IPickResult,ImageItemAdapter.ImageItemAdapterListener,ImageItemAdapter.ImageLongItemAdapterListener,VedioItemAdapter.VedioItemAdapterListener
       ,ViewPagerEx.OnPageChangeListener{
    private static final String PATIENT_ID_KEY = "patient_id";
    private static final String operationsItem = "operationsItem";
    static OperationsItem operationItem;
    Integer op_id;

    private int mOperationId;
    private static final String PATIENT_KEY = "patient_key";
    private static final int REQUEST_CODE_CHOOSE = 5;

    ImageItemAdapter imageItemAdapter;
    VedioItemAdapter vedioItemAdapter;
    EditOperationPresenterImp presenter;
    @BindView(R.id.operation_text)
    EditText operation_name_text;
    @BindView(R.id.date_text)
    EditText date_text;
    @BindView(R.id.steps_text)
    EditText steps_text;
    @BindView(R.id.person_text)
    EditText person_text;
    @BindView(R.id.follow_text)
    EditText follow_text;
    @BindView(R.id.btnEditOperation)
    Button btnEditOperation;
    @BindView(R.id.btnDeleteOperation)
    Button btnDeleteOperation ;
    @BindView(R.id.btnUploadImagesOp)
    Button btnUploadImagesOp;
    @BindView(R.id.btnUploadVediosOp)
    Button btnUploadVediosOp;

    @BindView(R.id.progress)
    ProgressBar mProgress;

    @BindView(R.id.recyclerViewItemUploadImages)
    RecyclerView recyclerViewItemUploadImages;
    @BindView(R.id.recyclerViewItemUploadVedios)
    RecyclerView recyclerViewItemUploadVedios;

    Context mContext;

    public static void start(Context context, int patientId, OperationsItem operationsItemO) {
        Intent starter = new Intent(context, EditOperationActivity.class);
        starter.putExtra(PATIENT_ID_KEY, patientId);
        starter.putExtra(operationsItem, operationsItemO);
        operationItem = operationsItemO;
        context.startActivity(starter);
    }
    List<MediaItem> mediaItems = null;
    List<MediaItem> mediaItemsImages = new ArrayList<MediaItem>();
    List<MediaItem> mediaItemsVedios = new ArrayList<MediaItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_operation);

        mContext =this;
        presenter = new EditOperationPresenterImp(this);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (getIntent().getIntExtra(PATIENT_ID_KEY, 0) != 0)
        {
            mOperationId = getIntent().getIntExtra(PATIENT_ID_KEY, 0);
        } else
        {
            throw new RuntimeException("INVALID PATIENT ID");
        }
        if (extras != null)
        {
            op_id = operationItem.getId();
            operation_name_text.setText(operationItem.getName());
            date_text.setText(operationItem.getDate());
            steps_text.setText(operationItem.getSteps());
            person_text.setText(operationItem.getPersons());
            follow_text.setText(operationItem.getFollow_up());
            mediaItems = operationItem.getMediaItems();
        }
        for(int i=0;i<mediaItems.size();i++)
        {
            if (mediaItems.get(i).getType()==1)
            {
                mediaItemsImages.add(mediaItems.get(i));
            }else if(mediaItems.get(i).getType()==2)
            {
                mediaItemsVedios.add(mediaItems.get(i));
            }
        }

        showImages(mediaItemsImages);
        showVedios(mediaItemsVedios);

        btnEditOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OperationsItem operationsItem = new OperationsItem();
                String name = operation_name_text.getText().toString();
                String date = date_text.getText().toString();
                String steps = steps_text.getText().toString();
                String persons = person_text.getText().toString();
                String followup = follow_text.getText().toString();
                operationsItem.setName(name);
                operationsItem.setDate(date);
                operationsItem.setSteps(steps);
                operationsItem.setPersons(persons);
                operationsItem.setFollow_up(followup);

                if (!name.isEmpty()) {
                    presenter.EditItemToServer(mOperationId, mOperationId, operationsItem);
                    mProgress.setVisibility(View.VISIBLE);
                }
            }
        });

        btnDeleteOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.deleteItemOperation(mOperationId);
                //PatientProfileActivity.start(mContext,operationItem.getPatient_id());
            }
            }
        );
        btnUploadImagesOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, 100);
                showPickDialog(true);
            }
        });
        btnUploadVediosOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                i.setType("video/");
//                startActivityForResult(i, 100);
                showPickDialog(false);

            }
        });
    }
    private void showPickDialog(boolean isPhoto) {
       /* if (isPhoto)
            PickImageDialog.build(new PickSetup()).show(this);
        else
            PickImageDialog.build(new PickSetup().setVideo(true)).show(this);*/
        Matisse.from(EditOperationActivity.this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    AlertDialog mImagesDialog;

    SliderLayout mSliderLayout;

    private void showImagesDialog(int id)
    {
        String[] mediaItemsUrl=new String[mediaItems.size()];
        for(int i=0;i<mediaItems.size();i++)
        {
            // mediaItemsUrl.add(i,mediaItems.get(i).getUrl());
            MediaItem m =mediaItems.get(i);
            String url=m.getUrl();
            mediaItemsUrl[i]=url;
        }
        new ImageViewer.Builder(this, mediaItemsUrl)
                .setStartPosition(id)
                .show();
       /* if (mImagesDialog == null) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(this).inflate(R.layout.media_popup, null);
            mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
            for (MediaItem item : mediaItemsImages) {
                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView.image(item.getUrl());
                mSliderLayout.addSlider(textSliderView);
            }
            mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mSliderLayout.setCustomAnimation(new DescriptionAnimation());
            mSliderLayout.addOnPageChangeListener(this);

            mImagesDialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();
            mImagesDialog.show();
        } else
            mImagesDialog.show();*/
    }


    @Override
    public void setOperationsUpdateSucessfull() {
        Toast.makeText(this, "Operations Updated Sucessfully", Toast.LENGTH_SHORT).show();
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void setOperationsUpdateFailure() {
        Toast.makeText(this, "Can't Update Operations ", Toast.LENGTH_SHORT).show();
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void setOperationMediaSuccess() {
        Toast.makeText(this,"Images Uploaded successfully",Toast.LENGTH_SHORT).show();
        mProgress.setVisibility(View.GONE);
        //TODO: Update Images List
    }

    @Override
    public void setOperationMediaFailure() {
        Toast.makeText(this,"Can't upload images",Toast.LENGTH_SHORT).show();
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void setItemDeleteSuccessful()
    {
        Toast.makeText(this,"Operation Deleted sucessfully",Toast.LENGTH_SHORT).show();
        mProgress.setVisibility(View.GONE);
    }
    @Override
    public void setItemDeleteFailure()
    {
        Toast.makeText(this,"Can't Delete Operation ",Toast.LENGTH_SHORT).show();
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void showImages(List<MediaItem> mediaItems) {
        if (mediaItems != null) {
            recyclerViewItemUploadImages.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL,false));
            imageItemAdapter = new ImageItemAdapter(mContext, mediaItems, this,this);
            recyclerViewItemUploadImages.setAdapter(imageItemAdapter);
        }
    }
    @Override
    public void showVedios(List <MediaItem> mediaItems)
    {
        if (mediaItems != null) {
            recyclerViewItemUploadVedios.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL,false));
            vedioItemAdapter = new VedioItemAdapter(mContext, mediaItems, this);
            recyclerViewItemUploadVedios.setAdapter(vedioItemAdapter);
        }
    }

    @Override
    public void onItemImageClicked(int id,MediaItem clickedItem)
    {
        showImagesDialog(id);
    }

    @Override
    public void onItemVedioClicked(int id ,MediaItem mediaItem)
    {
        String url = mediaItem.getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "video/*");
        startActivity(intent);
    }

    @Override
    public void onItemImageClickedLong(int adapterPos,int mediaId)
    {
        AlertDialog alertDialog=AskOption(mContext,adapterPos,mediaId,false);
        alertDialog.show();
    }

    @Override
    public void onItemVedioClickedLong(int adapterPos,int mediaId)
    {
        AlertDialog alertDialog=AskOption(mContext,adapterPos,mediaId,true);
        alertDialog.show();
    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = new String[1];
        proj[0] = MediaStore.Images.Media.DATA;
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
//            ArrayList<String> paths = new ArrayList<>();
//            paths.add(getRealPathFromURI(data.getData()));
//            presenter.uploadMediaToServer(op_id,paths);
//        }
    }*/

    List<Uri> mSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            ArrayList<String> paths = new ArrayList<>();
            for (Uri file : mSelected) {
                try {
                    paths.add(getRealPathFromURI(file));
                }catch (Exception exc) {
                    Toast.makeText(this,"Failed to encode file",
                            Toast.LENGTH_SHORT).show();
                }
            }
            if (paths.size() > 0) {
               // presenter.uploadMediaToServer(obj_id, paths, owner);
                presenter.uploadMediaToServer(op_id, paths);
                mProgress.setVisibility(View.VISIBLE);

            } else
                Toast.makeText(this, "Failed to encode video", Toast.LENGTH_SHORT)
                        .show();
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        ArrayList<String> paths = new ArrayList<>();
        Uri file = pickResult.getUri();
        if (pickResult.getPickType() == EPickType.CAMERA)
            paths.add(file.getPath());
        else {
            try {
                paths.add(getRealPathFromURI(file));
            } catch (Exception exc) {
                Toast.makeText(this,"Can't Upload Video",Toast.LENGTH_SHORT).show();
            }
        }
        presenter.uploadMediaToServer(op_id,paths);
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private AlertDialog AskOption(final Context mContext,final int adapterPos, final int mediaId,final boolean mediaFlag)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(mContext)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Are you Sure You want to delete this Item ? ")
                .setIcon(R.mipmap.ic_launcher2)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        try
                        {
                            // complainItem.getMediaItems().
                            presenter.deleteMediaItem(mediaId);
                            if(mediaFlag==false) //image flage
                            {
                                mediaItemsImages.remove(adapterPos);
                                imageItemAdapter.notifyItemRemoved(adapterPos);
                                imageItemAdapter.notifyItemRangeChanged(adapterPos,mediaItemsImages.size());
                            }
                            else // vedio
                            {
                                mediaItemsVedios.remove(adapterPos);
                                vedioItemAdapter.notifyItemRemoved(adapterPos);
                                vedioItemAdapter.notifyItemRangeChanged(adapterPos,mediaItemsVedios.size());
                            }
                            //HomeActivity.start(mContext);

                        }

                        catch (Exception e)
                        {
                            System.out.println("");
                        }

                    }

                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // mSwipeRefreshLayout.setRefreshing(false);
                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }


}
