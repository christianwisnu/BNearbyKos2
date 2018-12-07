package control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.chris.nearbykos2.R;

public class Utils {
    
    public static final String TAG = "Utils";
    public static final String CACHE_DIR_NAME = "__vimeo_v_cache";
    public static final ImageLoader imgloader = ImageLoader.getInstance();
    public enum VideoQuality { MOBILE, SD, HD };
    public static final float BITMAP_SCALE = 0.4f;
    public static final float BLUR_RADIUS = 7.5f;
    private static File cacheDir = null;
    private static boolean cacheDirCreated = false;
    public static Typeface fontsStyle;
    private static SimpleDateFormat srcFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static SimpleDateFormat srcDate = new SimpleDateFormat("yyyy-MM-dd");

    // ------------------------------ API --------------------------------------

    public static void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static String adaptTags(String[] tags, String noneText, String delimiter) {
        if (tags.length == 0) return noneText;
        if (tags.length == 1) return tags[0];
        final StringBuffer result = new StringBuffer();
        for (int i = 0; i < (tags.length - 1); i++) {
            result.append(tags[i]).append(delimiter);
        }
        result.append(tags[tags.length - 1]);
        return result.toString();
    }

    public static File createCacheDir(Context context, String dirName)  {
        File preparedDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            preparedDir = context.getDir(dirName /* + UUID.randomUUID().toString()*/, Context.MODE_PRIVATE);
            Log.i(TAG, "Cache dir initialized at SD card " + preparedDir.getAbsolutePath());
        } else {
            preparedDir = context.getCacheDir();
            Log.i(TAG, "Cache dir initialized at phone storage " + preparedDir.getAbsolutePath());
        }
        if(!preparedDir.exists()) {
            Log.i(TAG, "Cache dir not existed, creating");
            preparedDir.mkdirs();
        }
        return preparedDir;
    }

    public static File getDefaultCacheDir(Context context)  {
        if (cacheDirCreated) return cacheDir;
        else {
            cacheDir = createCacheDir(context, CACHE_DIR_NAME);
            cacheDirCreated = true;
            return cacheDir;
        }
    }

    public static View getItemViewIfVisible(AdapterView<?> holder, int itemPos) {
        int firstPosition = holder.getFirstVisiblePosition();
        int wantedChild = itemPos - firstPosition;
        if (wantedChild < 0 || wantedChild >= holder.getChildCount()) return null;
        return holder.getChildAt(wantedChild);
    }

    private static long timeStringtoMilis(String time) {
        long milis = 0;

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date 	= sd.parse(time);
            milis 		= date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return milis;
    }

    public static String getHari(String tanggal){
        String[] a = tanggal.split(" ");
        String[] b = a[0].split("-");
        String year = b[0];
        String month = b[1];
        String date = b[2];
        SimpleDateFormat sdf= new SimpleDateFormat("EEEE");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.MONTH, Integer.valueOf(month));
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date));
        String hari="";
        if(sdf.format(cal.getTime()).equals("Sunday")){
            hari="Minggu";
        }else if(sdf.format(cal.getTime()).equals("Monday")){
            hari="Senin";
        }else if(sdf.format(cal.getTime()).equals("Tuesday")){
            hari="Selasa";
        }else if(sdf.format(cal.getTime()).equals("Wednesday")){
            hari="Rabu";
        }else if(sdf.format(cal.getTime()).equals("Thursday")){
            hari="Kamis";
        }else if(sdf.format(cal.getTime()).equals("Friday")){
            hari="Jumat";
        }else if(sdf.format(cal.getTime()).equals("Saturday")){
            hari="Sabtu";
        }
        return hari;
    }

    public static void GetImage(String url,ImageView img,Context context){
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .build();
        imgloader.init(ImageLoaderConfiguration.createDefault(context));
        imgloader.displayImage(url, img, options);
        return;
    }

    public static void GetCycleImage(String url,ImageView img,Context context){
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer((int) 50.5f))
                .showImageOnLoading(R.mipmap.ic_action_person)
                .showImageForEmptyUri(R.mipmap.ic_action_person)
                .showImageOnFail(R.mipmap.ic_action_person)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .build();
        imgloader.init(ImageLoaderConfiguration.createDefault(context));
        imgloader.displayImage(url, img, options);


        return;
    }
}
