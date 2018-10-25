package control;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.chris.nearbykos2.R;


/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.util</dd>
 * </dl>
 *
 * <code>Utils</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 3, 2010 10:06:22 PM 
 *
 */
public class Utils {
    
    // TODO: group with inner classes
        
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
    
    public static String validateShortcutOrId(final String shortcut) { 
        if (!shortcut.matches("^[\\d\\w_]+$")) throw new IllegalArgumentException("Not correct schortcut or _ID: " + shortcut);
        return shortcut;
    }
    
    public static String validateId(final String id) { 
        if (!id.matches("^\\d+$")) throw new IllegalArgumentException("Not correct _ID: " + id);
        return id;
    }
    
    public static String validateActionName(String action) {
        if (!action.matches("^[\\w_]+$")) throw new IllegalArgumentException("Not correct action name: " + action);
        return action;
    }
    
    public static String authorIdFromProfileUrl(String uploaderProfileUrl) {
        return uploaderProfileUrl.substring(17);
    }
      
    // ------------------------------ Adapt / Format ---------------------------
    
    public static String crop(String value, int howMuch) {
        return (value.length() <= howMuch) ? value : (value.substring(0, howMuch - 3) + "...");  
    }
    
    public static String quantity(Context context, int resId, int quantity) {
        return context.getResources().getQuantityString(resId, quantity, quantity);
    }
    
    /* public static String format(String source, String... params) {
        String result = source;
        int pos = 0;
        while (pos < params.length) {
            result = result.replaceAll("\\{" + params[pos++] + "\\}", params[pos++]);
        }
        return result;
    } */
    
    public static String adaptDuration(long duration) {
        final long remainder = duration % 60; 
        return ((duration - remainder) / 60) + ":" + ((remainder < 10) ? ("0" + remainder) : remainder); 
    }
    
    public static boolean adaptBoolean(int value) {
        return (value == 0) ? false : true;
    }
    
    // gets date in format yyyy-MM-dd hh:mm:ss
    // returns in dd MMM yyyy hh:mm format
    public static String TimeDate(String date) {
        try {
            return DateFormat.format("dd MMM yyyy kk:mm", srcFormat.parse(date)).toString();
        } catch (ParseException e) {
            return "## #### #### ##:##";
        }
    }
    
    public static String Date(String date) {
        try {
            return DateFormat.format("dd MMM yyyy", srcDate.parse(date)).toString();
        } catch (ParseException e) {
            return "## #### ####";
        }
    }
    
    public static String[] extractTags(String source) {
        final List<String> result = new LinkedList<String>();
        for (String tag: source.split(",")) {
            if (tag.trim().length() > 0) result.add(tag.trim());
        }
        return result.toArray(new String[result.size()]);
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
    
    public static String adaptTags(String[] tags, String noneText) {
        return adaptTags(tags, noneText, " / ");
    }
    
    public static String[] stringArrayFromJson(JSONArray jsonArr) throws JSONException {
        final String[] array = new String[jsonArr.length()];
        for (int i = 0; i < jsonArr.length(); i++) {
            array[i] = jsonArr.getString(i);
        }
        return array;
    }
        
    // ------------------------------ Network ----------------------------------
    
    public static int lookupHost(String hostname) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            return -1;
        }
        byte[] addrBytes;
        int addr;
        addrBytes = inetAddress.getAddress();
        addr = ((addrBytes[3] & 0xff) << 24)
                | ((addrBytes[2] & 0xff) << 16)
                | ((addrBytes[1] & 0xff) << 8)
                |  (addrBytes[0] & 0xff);
        return addr;
    }        
    
    // ------------------------ Files: Streams / Cache -------------------------
    
    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }    
    
    public static void copyFile(File oldLocation, File newLocation) throws IOException {
        if ( oldLocation.exists( )) {
            BufferedInputStream  reader = new BufferedInputStream( new FileInputStream(oldLocation) );
            BufferedOutputStream  writer = new BufferedOutputStream( new FileOutputStream(newLocation, false));
            try {
                byte[]  buff = new byte[8192];
                int numChars;
                while ( (numChars = reader.read(  buff, 0, buff.length ) ) != -1) {
                    writer.write( buff, 0, numChars );
                }
            } catch( IOException ex ) {
                throw new IOException("IOException when transferring " + oldLocation.getPath() + " to " + newLocation.getPath());
            } finally {
                try {
                    if ( reader != null ){                      
                        writer.close();
                        reader.close();
                    }
                } catch( IOException ex ){
                    Log.e(TAG, "Error closing files when transferring " + oldLocation.getPath() + " to " + newLocation.getPath() ); 
                }
            }
        } else {
            throw new IOException("Old location does not exist when transferring " + oldLocation.getPath() + " to " + newLocation.getPath() );
        }
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
        
        public static File newTempFile(Context context, String prefix, String suffix) throws IOException {
                return File.createTempFile(prefix, suffix, getDefaultCacheDir(context));
        }
        
        public static long computeFreeSpace() {
            File dataDir = Environment.getDataDirectory();
        StatFs stat = new StatFs(dataDir.getPath());
        return stat.getAvailableBlocks() * stat.getBlockSize();
        }
        
        // ------------------------ Views ------------------------------------------
        
        public static View getItemViewIfVisible(AdapterView<?> holder, int itemPos) {
            int firstPosition = holder.getFirstVisiblePosition();
            int wantedChild = itemPos - firstPosition;
            if (wantedChild < 0 || wantedChild >= holder.getChildCount()) return null;
            return holder.getChildAt(wantedChild);
        }
    
    public static void invalidateByPos(AdapterView<?> parent, int position) {
        final View itemView = getItemViewIfVisible(parent, position);
        Log.d(TAG, "Trying to invalidate view " + itemView + " at pos " + position + " ");        
        if (itemView != null) itemView.invalidate();        
    }
    
    public static void forcePostInvalidate(AdapterView<?> parent, int position) {
        final View itemView = getItemViewIfVisible(parent, position);
        if (itemView != null) itemView.postInvalidate();        
    }        
    

    public static void TypeFace(TextView tv, AssetManager asm,String fnt){
    
        fontsStyle=Typeface.createFromAsset(asm, fnt+".ttf"); 
        tv.setTypeface(fontsStyle);
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
    
    public static void CurrentTime(String date,TextView txtTime){
    	long now 			= System.currentTimeMillis();	
    	long longTimeAgo	= timeStringtoMilis(date);
    	PrettyTime prettyTime = new PrettyTime();
    	txtTime.setText(prettyTime.format(new Date(longTimeAgo)));	
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
    
    
	 public static void loadGifIntoImageView(ImageView ivImage, int rawId,Context context) {
           try {
               GifAnimationDrawable anim = new GifAnimationDrawable(context.getResources().openRawResource(rawId));
               ivImage.setImageDrawable(anim);
               ((GifAnimationDrawable) ivImage.getDrawable()).setVisible(true, true);
           } catch (NotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
	 
	 
		    
		    public static Bitmap getImageBitmapFromUrl(URL url)
		    { 
		      Bitmap bm = null; 
		      try { 
		        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		        if(conn.getResponseCode() != 200)
		        {
		          return bm;
		        }
		        conn.connect();
		        InputStream is = conn.getInputStream();

		        BufferedInputStream bis = new BufferedInputStream(is); 
		        try
		        {
		          bm = BitmapFactory.decodeStream(bis); 
		        }
		        catch(OutOfMemoryError ex)
		        {
		          bm = null;
		        }
		        bis.close(); 
		        is.close(); 
		      } catch (Exception e) {}
		      
		        return bm; 
		    }
		    
		    public static long getMinutesDifference(long timeStart,long timeStop){
	            long diff = timeStop - timeStart;
	            long diffMinutes = diff / (6 * 1000);

	            return  diffMinutes;
	        }
		    
		    public static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {
		        long now = System.currentTimeMillis();

		        Map<String, String> headers = response.headers;
		        long serverDate = 0;
		        String serverEtag = null;
		        String headerValue;

		        headerValue = headers.get("Date");
		        if (headerValue != null) {
		            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
		        }

		        serverEtag = headers.get("ETag");

		        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
		        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
		        final long softExpire = now + cacheHitButRefreshed;
		        final long ttl = now + cacheExpired;

		        Cache.Entry entry = new Cache.Entry();
		        entry.data = response.data;
		        entry.etag = serverEtag;
		        entry.softTtl = softExpire;
		        entry.ttl = ttl;
		        entry.serverDate = serverDate;
		        entry.responseHeaders = headers;

		        return entry;
		    }
		    


    /*
    @SuppressLint("InflateParams")
	public static void ViewMessageText(Context mContext,String Description,final AlertDialog ad){
    	
    
			LayoutInflater li = LayoutInflater.from(mContext);		
			View promptsView = li.inflate(R.layout.lytviewmessagetext, null);
		
		
			TextView txtDescription	= (TextView)promptsView.findViewById(R.id.txtDesMessageText);
			txtDescription.setText(Description);
			ImageButton btnClose	= (ImageButton)promptsView.findViewById(R.id.ImbCloseMessTex);
			ad.getWindow().getAttributes().windowAnimations=R.style.ViewMessageText;
			ad.setView(promptsView);
			
			btnClose.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ad.dismiss();
					
				}
			});
			ad.show();
			
		
			
    }
    
    @SuppressLint("InflateParams")
	public static void ViewImageZoom(Context mContext,String url,final AlertDialog ad){
    		LayoutInflater li = LayoutInflater.from(mContext);		
			View promptsView = li.inflate(R.layout.lytviewimagezoom, null);			
			ImageView imgZoom = (ImageView)promptsView.findViewById(R.id.ImgZoom);
			ImageButton btnClose	= (ImageButton)promptsView.findViewById(R.id.ImbCloseZoom);
			Utils.GetImage(url, imgZoom);
			ad.getWindow().getAttributes().windowAnimations=R.style.ViewMessageText;
			ad.setView(promptsView);
			
			btnClose.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ad.dismiss();
					
				}
			});
			ad.show();
			
		
			
    }
    
    public static void pushFragment(FragmentManager fm, int parentId, Fragment currentFrag, Fragment newFrag,String tag) {
        FragmentTransaction ft = fm.beginTransaction();
   //     ft.replace(parentId, newFrag, tag);
        ft.add(parentId, newFrag, tag);
        if(currentFrag!=null){
        	ft.hide(currentFrag);
        }
        ft.show(newFrag);
        ft.addToBackStack(null);
        ft.commit();
    }
       */ 
        
}
