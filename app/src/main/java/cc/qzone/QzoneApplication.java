package cc.qzone;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


public class QzoneApplication extends Application {

	public static QzoneApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "cc.qzone/cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCache(new UnlimitedDiscCache(cacheDir)) //自定义缓存路径
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}

}
