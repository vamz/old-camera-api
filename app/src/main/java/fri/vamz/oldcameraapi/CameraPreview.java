package fri.vamz.oldcameraapi;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {


    SurfaceHolder holder;
    private Camera camera;
    private List<Camera.Size> supportedPreviewSizes;

    CameraPreview(Context context, Camera camera) {
        super(context);

        this.camera = camera;

        holder = this.getHolder();
        holder.addCallback(this);

        // deprecated, needed for lower android versions
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

//    public void setCamera(Camera camera) {
//        if (mCamera == camera) { return; }
//
//        stopPreviewAndFreeCamera();
//
//        mCamera = camera;
//
//        if (mCamera != null) {
//            List<android.hardware.Camera.Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
//            supportedPreviewSizes = localSizes;
//            requestLayout();
//
//            try {
//                mCamera.setPreviewDisplay(holder);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // Important: Call startPreview() to start updating the preview
//            // surface. Preview must be started before you can take a picture.
//            mCamera.startPreview();
//        }
//    }

    /**
     * When this function returns, mCamera will be null.
     */
    private void stopPreviewAndFreeCamera() {

        if (camera != null) {
            // Call stopPreview() to stop updating the preview surface.
            camera.stopPreview();

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            camera.release();

            camera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (camera == null) {

            return;
        }

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d("camapp", "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (holder.getSurface() == null){
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (Exception e){
            Log.d("camapp", "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (camera != null) {
            // Call stopPreview() to stop updating the preview surface.
            camera.stopPreview();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

}