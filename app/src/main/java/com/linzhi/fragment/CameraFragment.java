package com.linzhi.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linzhi.R;
import com.linzhi.widget.SquareSurfacePreview;

import java.io.IOException;
import java.util.List;

import static android.hardware.Camera.CameraInfo;
import static android.hardware.Camera.PictureCallback;
import static android.hardware.Camera.ShutterCallback;
import static android.hardware.Camera.getCameraInfo;
import static android.hardware.Camera.open;

/**
 * 相机详细操作
 * <p>
 * 预览界面适应公司需求，为方形图
 * 可以切换摄像头
 * 拍照后 图片自动旋转处理，
 * <p>
 * Created by sjy on 2016/11/17.
 */

public class CameraFragment extends Fragment implements SurfaceHolder.Callback, PictureCallback {

    //常量
    public static final String TAG = "CameraFragment";//CameraFragment.class.getSimpleName()


    /*
     * 控件
     */
    private SquareSurfacePreview mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    //拍照三个按钮
    private LinearLayout camera_tools_view;
    private ImageView takePhotoBtn;//拍照按钮
    private ImageView swapCameraBtn;//切换按钮

    //变量
    private int wideDiffer;//宽高差
    private int mCoverHeight;//宽高差的一半
    private int mSurfaceView_height;
    private int mCameraID = 0;
    private String mFlashMode;
    private Camera mCamera = null;
    private CameraOrientationListener mOrientationListener;
    private int mDisplayOrientation;
    private int mLayoutOrientation;

    public static Fragment newInstance() {
        return new CameraFragment();
    }

    public CameraFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOrientationListener = new CameraOrientationListener(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCameraID = getBackCameraID();//获取后置摄像头的mCameraID
        mFlashMode = Camera.Parameters.FLASH_MODE_AUTO;//auto

        initMyView(view);

        //观察者模式
        ViewTreeObserver observer = mSurfaceView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int mSurfaceView_width = mSurfaceView.getWidth();
                mSurfaceView_height = mSurfaceView.getHeight();
                mCoverHeight = (mSurfaceView_width - mSurfaceView_height) / 2;

                wideDiffer = mSurfaceView_width - mSurfaceView_height;

                Log.d(TAG, "preview width " + mSurfaceView_width + " height " + mSurfaceView_height + "mCoverHeight=" + mCoverHeight);

                //                    topCoverView.getLayoutParams().height = mCoverHeight;
                //                    btnCoverView.getLayoutParams().height = mCoverHeight;
                //设置界面
                ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
                lp.width = mSurfaceView_height;
                lp.height =mSurfaceView_height;
                mSurfaceView.setLayoutParams(lp);

                ViewGroup.LayoutParams toolsView = camera_tools_view.getLayoutParams();
                toolsView.width = wideDiffer;
                toolsView.height = mSurfaceView_height;
                camera_tools_view.setLayoutParams(toolsView);

                //sdK版本设置（可忽略选项）
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mSurfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mSurfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        initListener();
    }

    //初始化控件
    private void initMyView(View view) {
        mSurfaceView = (SquareSurfacePreview) view.findViewById(R.id.camera_preview_view);
        camera_tools_view = (LinearLayout) view.findViewById(R.id.camera_tools_view);
        mSurfaceView.getHolder().addCallback(CameraFragment.this);

        //拍照三个按钮
        takePhotoBtn = (ImageView) view.findViewById(R.id.capture_image_button);//拍照按钮
        swapCameraBtn = (ImageView) view.findViewById(R.id.change_camera);

        mOrientationListener.enable();//激活监听

    }

    private void initListener() {
        //拍照按钮
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        //切换摄像头
        swapCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCameraID == CameraInfo.CAMERA_FACING_FRONT) {
                    mCameraID = getBackCameraID();
                } else {
                    mCameraID = getFrontCameraID();
                }
                Log.d(TAG, "切换摄像头mCamera=" + (mCamera == null));
                restartPreview();
            }
        });
    }

    /**
     * SurfaceHolder.Callback复写方法
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        mSurfaceHolder = holder;
        getCamera(mCameraID);
        Log.d(TAG, "surfaceCreated mCamera == null =" + (mCamera == null));
        startCameraPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case 1:
                Uri imageUri = data.getData();
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * Start the camera preview
     */
    private void startCameraPreview() {
        determineDisplayOrientation();
        setupCamera();

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Can't start camera preview due to IOException " + e);
            e.printStackTrace();
        }
    }

    /**
     * Restart
     */
    private void restartPreview() {
        stopCameraPreview();
        mCamera.release();
        getCamera(mCameraID);
        startCameraPreview();
    }

    /**
     * Stop the camera preview
     */
    private void stopCameraPreview() {
        mCamera.stopPreview();

        mCamera.setPreviewCallback(null);//

        mSurfaceView.setCamera(null);
    }

    //初始化Camera
    private void getCamera(int cameraID) {
        Log.d(TAG, "get camera with id " + cameraID);

        try {
            mCamera = open(cameraID);
            mSurfaceView.setCamera(mCamera);
        } catch (Exception e) {
            Log.d(TAG, "Can't open camera with id " + cameraID + "error:" + e.getMessage());
        }
    }

    /**
     * 相机角度，预览前设置
     * Determine the current display orientation and rotate the camera preview
     * accordingly
     */
    private void determineDisplayOrientation() {
        CameraInfo cameraInfo = new CameraInfo();
        getCameraInfo(mCameraID, cameraInfo);

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: {
                Log.d(TAG, "degrees = 0");
                degrees = 0;
                break;
            }
            case Surface.ROTATION_90: {
                Log.d(TAG, "degrees = 90");
                degrees = 90;
                break;
            }
            case Surface.ROTATION_180: {
                Log.d(TAG, "degrees = 180");

                degrees = 180;
                break;
            }
            case Surface.ROTATION_270: {
                Log.d(TAG, "degrees = 270");
                degrees = 270;
                break;
            }
        }
        int displayOrientation;
        // Camera direction
        if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
            // Orientation is angle of rotation when facing the camera for
            // the camera image to match the natural orientation of the device
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }
        mDisplayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        mLayoutOrientation = degrees;

        Log.d(TAG, "调整相机角度--determineDisplayOrientation--displayOrientation = " + displayOrientation);
        mCamera.setDisplayOrientation(displayOrientation);//调整相机角度
    }

    /**
     * 设置相机参数
     */
    private void setupCamera() {
        Log.d(TAG, "setupCamera: ");
        // Never keep a global parameters
        Camera.Parameters parameters = mCamera.getParameters();

        //设置最佳预览尺寸 和图片尺寸
        Camera.Size bestPreviewSize = determineBestPreviewSize(parameters);
        Camera.Size bestPictureSize = determineBestPictureSize(parameters);

        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
        parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);


        Log.d(TAG, "最佳预览尺寸--setupCamera--PreviewSize---" + "width=" + bestPreviewSize.width + "--height=" + bestPreviewSize.height);
        Log.d(TAG, "最佳图片尺寸--setupCamera--PictureSize---" + "width=" + bestPictureSize.width + "--height=" + bestPictureSize.height);

        // Set continuous picture focus, if it's supported
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        // Lock in the changes
        mCamera.setParameters(parameters);
    }


    //相机预览尺寸
    private Camera.Size determineBestPreviewSize(Camera.Parameters parameters) {
        return determineBestSize(parameters.getSupportedPreviewSizes());
    }

    //拍照后预览尺寸
    private Camera.Size determineBestPictureSize(Camera.Parameters parameters) {
        return determineBestSize(parameters.getSupportedPictureSizes());
    }

    /**
     * 设置最佳宽高比尺寸 横屏时不使用，设置成固定尺寸即可
     */

    private Camera.Size determineBestSize(List<Camera.Size> sizes) {
        Camera.Size bestSize = null;
        Camera.Size size;
        int numOfSizes = sizes.size();
        for (int i = 0; i < numOfSizes; i++) {
            size = sizes.get(i);

            boolean isDesireRatio = (size.width / 4) == (size.height / 3);//最佳4：3尺寸选择 (size.width / 4) == (size.height / 3)
            boolean isBetterSize = (bestSize == null) || size.width > bestSize.width;//选择最佳尺寸中最大的尺寸

            if (isDesireRatio && isBetterSize) {
                bestSize = size;
                //选中的最佳尺寸
                Log.d(TAG, "determineBestSize: bestSize.width" + (bestSize.width) + "\nbestSize.height:" + bestSize.height);
            }
        }

        if (bestSize == null) {
            Log.d(TAG, "cannot find the best camera size");
            return sizes.get(sizes.size() - 1);
        }

        return bestSize;
    }

    //获取前置cameraid
    private int getFrontCameraID() {
        return getCameraId(CameraInfo.CAMERA_FACING_FRONT);
    }

    //获取后置cameraid
    private int getBackCameraID() {
        return getCameraId(CameraInfo.CAMERA_FACING_BACK);
    }

    /**
     * @param tagInfo
     * @return 得到特定camera info的id
     */
    private int getCameraId(int tagInfo) {
        CameraInfo cameraInfo = new CameraInfo();
        // 开始遍历摄像头，得到camera info
        int cameraId, cameraCount;
        for (cameraId = 0, cameraCount = Camera.getNumberOfCameras(); cameraId < cameraCount; cameraId++) {
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == tagInfo) {
                break;
            }
        }
        return cameraId;
    }

    /**
     * Take a picture
     */
    private void takePicture() {
        mOrientationListener.rememberOrientation();

        // Shutter callback occurs after the image is captured. This can
        // be used to trigger a sound to let the user know that image is taken
        ShutterCallback shutterCallback = null;

        // Raw callback occurs when the raw image data is available
        PictureCallback raw = null;

        // postView callback occurs when a scaled, fully processed
        // postView image is available.
        PictureCallback postView = null;

        // jpeg callback occurs when the compressed image is available
        mCamera.takePicture(shutterCallback, raw, postView, this);
    }

    /**
     * 拍照回调函数
     *
     * @param data
     * @param camera
     */
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        int rotation = (mDisplayOrientation
                + mOrientationListener.getRememberedNormalOrientation()
                + mLayoutOrientation) % 360;
        Log.d(TAG, "onPictureTaken--rotation=" + rotation);

        //        Bitmap bitmap = ImageUtility.rotatePicture(getActivity(), rotation, data);
        //        Uri uri = ImageUtility.savePicture(getActivity(), bitmap);
        Log.d(TAG, "mCoverHeight=" + mCoverHeight + "\nmSurfaceView_height=" + mSurfaceView_height);

        //        if(rotation == 180 || mCameraID == CameraInfo.CAMERA_FACING_FRONT){//对一种情况图做处理
        //            rotation = 0;
        //        }

        //拍照后图片预览和处理EditSavePhotoFragment
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                EditSavePhotoFragment.newInstance(data, rotation, mCoverHeight, mSurfaceView_height),
                EditSavePhotoFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 方向监听
     * When orientation changes, onOrientationChanged(int) of the listener will be called
     */
    private static class CameraOrientationListener extends OrientationEventListener {

        private int mCurrentNormalizedOrientation;
        private int mRememberedNormalOrientation;

        public CameraOrientationListener(Context context) {
            super(context, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation != ORIENTATION_UNKNOWN) {
                mCurrentNormalizedOrientation = normalize(orientation);
            }
        }

        private int normalize(int degrees) {
            if (degrees > 315 || degrees <= 45) {

                return 0;
            }

            if (degrees > 45 && degrees <= 135) {
                return 90;
            }

            if (degrees > 135 && degrees <= 225) {
                return 180;
            }

            if (degrees > 225 && degrees <= 315) {
                return 270;
            }

            throw new RuntimeException("The physics as we know them are no more. Watch out for anomalies.");
        }

        public void rememberOrientation() {
            mRememberedNormalOrientation = mCurrentNormalizedOrientation;
            Log.d(TAG, "CameraOrientationListener--rememberOrientation--mRememberedNormalOrientation=" + mRememberedNormalOrientation);
        }

        public int getRememberedNormalOrientation() {
            Log.d(TAG, "CameraOrientationListener--getRememberedNormalOrientation--mRememberedNormalOrientation=" + mRememberedNormalOrientation);
            return mRememberedNormalOrientation;
        }
    }

    @Override
    public void onStop() {
        mOrientationListener.disable();
        // stop the preview
        stopCameraPreview();
        mCamera.release();
        super.onStop();
    }

}
