package com.tiendas3b.almacen.sync.adapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.ArticleDecrease;
import com.tiendas3b.almacen.db.dao.ArticleTransference;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.dao.TimetableReceipt;
import com.tiendas3b.almacen.db.dao.Unit;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
/**
 * Created by dfa on 01/08/2016.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";

//    /**
//     * URL to fetch content from during a sync.
//     *
//     * <p>This points to the Android Developers Blog. (Side note: We highly recommend reading the
//     * Android Developer Blog to stay up to date on the latest Android platform developments!)
//     */
//    private static final String FEED_URL = "http://android-developers.blogspot.com/atom.xml";
//
//    /**
//     * Network connection timeout, in milliseconds.
//     */
//    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds
//
//    /**
//     * Network read timeout, in milliseconds.
//     */
//    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;
    private Tiendas3bClient httpService;
    private IDatabaseManager databaseManager;
    private CountDownLatch doneSignal;
    private static final int TOTAL = 1;
    private GlobalState mContext;

//    /**
//     * Project used when querying content provider. Returns all known fields.
//     */
//    private static final String[] PROJECTION = new String[] {
//            FeedContract.Entry._ID,
//            FeedContract.Entry.COLUMN_NAME_ENTRY_ID,
//            FeedContract.Entry.COLUMN_NAME_TITLE,
//            FeedContract.Entry.COLUMN_NAME_LINK,
//            FeedContract.Entry.COLUMN_NAME_PUBLISHED};
//
//    // Constants representing column positions from PROJECTION.
//    public static final int COLUMN_ID = 0;
//    public static final int COLUMN_ENTRY_ID = 1;
//    public static final int COLUMN_TITLE = 2;
//    public static final int COLUMN_LINK = 3;
//    public static final int COLUMN_PUBLISHED = 4;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = (GlobalState) context;
        mContentResolver = mContext.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = (GlobalState) context;
        mContentResolver = mContext.getContentResolver();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
//        Log.e(TAG, "Beginning network synchronization");


//        mContext = (GlobalState) getApplicationContext();
        databaseManager = new DatabaseManager(mContext);
        httpService = mContext.getHttpServiceWithAuth();

        doneSignal = new CountDownLatch(TOTAL);//num catalogs
//            downloadTimetableReceiptCat(mContext);
//        downloadObservationsTypeCat();
//        downloadUnitCat();
//        downloadRegionCat();
//        downloadStoreCat();
//        downloadProviderCat();
//        downloadArticleDecreaseViewCat();
//        downloadArticleTransferenceViewCat();
//        downloadViewArticleCat();
        downloadInputTransferenceCat();// sólo se usa este, te comente que sólo se usa para las trasferencias de entrada
        //todos los demás están ahí por si gustar invocarlos también. yo los quité porque los metí en la parte de configuración, para que sea a demanada

        try {
            doneSignal.await();
            databaseManager.closeDbConnections();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        try {
//            final URL location = new URL(FEED_URL);
//            InputStream stream = null;
//
//            try {
//                Log.i(TAG, "Streaming data from network: " + location);
//                stream = downloadUrl(location);
//                updateLocalFeedData(stream, syncResult);
//                // Makes sure that the InputStream is closed after the app is
//                // finished using it.
//            } finally {
//                if (stream != null) {
//                    stream.close();
//                }
//            }
//        } catch (MalformedURLException e) {
//            Log.wtf(TAG, "Feed URL is malformed", e);
//            syncResult.stats.numParseExceptions++;
//            return;
//        } catch (IOException e) {
//            Log.e(TAG, "Error reading from network: " + e.toString());
//            syncResult.stats.numIoExceptions++;
//            return;
//        } catch (XmlPullParserException e) {
//            Log.e(TAG, "Error parsing feed: " + e.toString());
//            syncResult.stats.numParseExceptions++;
//            return;
//        } catch (ParseException e) {
//            Log.e(TAG, "Error parsing feed: " + e.toString());
//            syncResult.stats.numParseExceptions++;
//            return;
//        } catch (RemoteException e) {
//            Log.e(TAG, "Error updating database: " + e.toString());
//            syncResult.databaseError = true;
//            return;
//        } catch (OperationApplicationException e) {
//            Log.e(TAG, "Error updating database: " + e.toString());
//            syncResult.databaseError = true;
//            return;
//        }
//        Log.e(TAG, "Network synchronization complete");
    }

    private void downloadUnitCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<Unit>> call = httpService.getUnits();
        call.enqueue(new Callback<List<Unit>>() {
            @Override
            public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
                if (response.isSuccessful()) {
                    List<Unit> list = response.body();
                    databaseManager.insertOrReplaceInTx(list.toArray(new Unit[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Unit>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadViewArticleCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<VArticle>> call = httpService.getViewArticles();
        call.enqueue(new Callback<List<VArticle>>() {
            @Override
            public void onResponse(Call<List<VArticle>> call, Response<List<VArticle>> response) {
                if (response.isSuccessful()) {
                    List<VArticle> list = response.body();
                    databaseManager.insertOrReplaceInTx(list.toArray(new VArticle[list.size()]));
                    Preferences p = new Preferences(mContext);
                    p.setSharedStringSafe(Preferences.KEY_LAST_UPDATE, new SimpleDateFormat(DateUtil.DD_MM_YYYY_HH_MM, Locale.getDefault()).format(new Date()));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<VArticle>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadInputTransferenceCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<InputTransference>> call = httpService.getInputTransference(mContext.getRegion());
        call.enqueue(new Callback<List<InputTransference>>() {
            @Override
            public void onResponse(Call<List<InputTransference>> call, Response<List<InputTransference>> response) {
                if (response.isSuccessful()) {
                    List<InputTransference> list = response.body();
                    databaseManager.truncate(InputTransference.class);
                    if(list != null)
                    databaseManager.insertOrReplaceInTx(list.toArray(new InputTransference[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<InputTransference>> call, Throwable t) {
                Log.e(TAG, "getInputTransference error");
                doneSignal.countDown();
            }
        });
    }

    private void downloadObservationsTypeCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<ObservationType>> call = httpService.getObservationsType();
        call.enqueue(new Callback<List<ObservationType>>() {
            @Override
            public void onResponse(Call<List<ObservationType>> call, Response<List<ObservationType>> response) {
                if (response.isSuccessful()) {
                    List<ObservationType> list = response.body();
                    databaseManager.insertOrReplaceInTx(list.toArray(new ObservationType[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ObservationType>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_stores));
                doneSignal.countDown();
            }
        });
    }

    private void downloadStoreCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<Store>> call = httpService.getStores(mContext.getRegion());
        call.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (response.isSuccessful()) {
                    List<Store> providers = response.body();
                    databaseManager.insertOrReplaceInTx(providers.toArray(new Store[providers.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_stores));
                doneSignal.countDown();
            }
        });
    }

    private void downloadArticleDecreaseViewCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<ArticleDecrease>> call = httpService.getViewArticlesDecrease(mContext.getRegion());
        call.enqueue(new Callback<List<ArticleDecrease>>() {
            @Override
            public void onResponse(Call<List<ArticleDecrease>> call, Response<List<ArticleDecrease>> response) {
                if (response.isSuccessful()) {
                    List<ArticleDecrease> items = response.body();
                    if(items != null) {
                        databaseManager.insertOrReplaceInTx(items.toArray(new ArticleDecrease[items.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ArticleDecrease>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_articles_view));
                doneSignal.countDown();
            }
        });
    }

    private void downloadArticleTransferenceViewCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<ArticleTransference>> call = httpService.getViewArticlesTransference(mContext.getRegion());
        call.enqueue(new Callback<List<ArticleTransference>>() {
            @Override
            public void onResponse(Call<List<ArticleTransference>> call, Response<List<ArticleTransference>> response) {
                if (response.isSuccessful()) {
                    List<ArticleTransference> items = response.body();
                    if(items != null) {
                        databaseManager.insertOrReplaceInTx(items.toArray(new ArticleTransference[items.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ArticleTransference>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_articles_transference_view));
                doneSignal.countDown();
            }
        });
    }

    private void downloadTimetableReceiptCat() {// etc
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<TimetableReceipt>> call = httpService.getTimetableReceiptCatalog(mContext.getRegion());
        call.enqueue(new Callback<List<TimetableReceipt>>() {
            @Override
            public void onResponse(Call<List<TimetableReceipt>> call, Response<List<TimetableReceipt>> response) {
                if (response.isSuccessful()) {
                    List<TimetableReceipt> providers = response.body();
                    if(providers != null) {
                        databaseManager.insertOrReplaceInTx(providers.toArray(new TimetableReceipt[providers.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<TimetableReceipt>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_timetable_receipt));
                doneSignal.countDown();
            }
        });
    }

    private void downloadProviderCat() {//tampoco
        Call<List<Provider>> call = httpService.getProviderCatalog();
        call.enqueue(new Callback<List<Provider>>() {
            @Override
            public void onResponse(Call<List<Provider>> call, Response<List<Provider>> response) {
                if (response.isSuccessful()) {
                    List<Provider> providers = response.body();
                    databaseManager.insertOrReplaceInTx(providers.toArray(new Provider[providers.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Provider>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_provider));
                doneSignal.countDown();
            }
        });
    }

    private void downloadRegionCat() {//no se usa está en gris
        Call<List<Region>> call = httpService.getRegionCatalog();
        call.enqueue(new Callback<List<Region>>() {
            @Override
            public void onResponse(Call<List<Region>> call, Response<List<Region>> response) {
                if (response.isSuccessful()) {
                    List<Region> entities = response.body();
                    databaseManager.insertOrReplaceInTx(entities.toArray(new Region[entities.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Region>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_region));
                doneSignal.countDown();
            }
        });
    }

//    /**
//     * Read XML from an input stream, storing it into the content provider.
//     *
//     * <p>This is where incoming data is persisted, committing the results of a sync. In order to
//     * minimize (expensive) disk operations, we compare incoming data with what's already in our
//     * database, and compute a merge. Only changes (insert/update/delete) will result in a database
//     * write.
//     *
//     * <p>As an additional optimization, we use a batch operation to perform all database writes at
//     * once.
//     *
//     * <p>Merge strategy:
//     * 1. Get cursor to all items in feed<br/>
//     * 2. For each item, check if it's in the incoming data.<br/>
//     *    a. YES: Remove from "incoming" list. Check if data has mutated, if so, perform
//     *            database UPDATE.<br/>
//     *    b. NO: Schedule DELETE from database.<br/>
//     * (At this point, incoming database only contains missing items.)<br/>
//     * 3. For any items remaining in incoming list, ADD to database.
//     */
//    public void updateLocalFeedData(final InputStream stream, final SyncResult syncResult)
//            throws IOException, XmlPullParserException, RemoteException, OperationApplicationException, ParseException {
//        final FeedParser feedParser = new FeedParser();
//
//        Log.i(TAG, "Parsing stream as Atom feed");
//        final List<FeedParser.Entry> entries = feedParser.parse(stream);
//        Log.i(TAG, "Parsing complete. Found " + entries.size() + " entries");
//
//
//        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
//
//        // Build hash table of incoming entries
//        HashMap<String, FeedParser.Entry> entryMap = new HashMap<String, FeedParser.Entry>();
//        for (FeedParser.Entry e : entries) {
//            entryMap.put(e.id, e);
//        }
//
//        final ContentResolver contentResolver = getContext().getContentResolver();
//        // Get list of all items
//        Log.i(TAG, "Fetching local entries for merge");
//        Uri uri = FeedContract.Entry.CONTENT_URI; // Get all entries
//        Cursor c = contentResolver.query(uri, PROJECTION, null, null, null);
//        assert c != null;
//        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");
//
//        // Find stale data
//        int id;
//        String entryId;
//        String title;
//        String link;
//        long published;
//        while (c.moveToNext()) {
//            syncResult.stats.numEntries++;
//            id = c.getInt(COLUMN_ID);
//            entryId = c.getString(COLUMN_ENTRY_ID);
//            title = c.getString(COLUMN_TITLE);
//            link = c.getString(COLUMN_LINK);
//            published = c.getLong(COLUMN_PUBLISHED);
//            FeedParser.Entry match = entryMap.get(entryId);
//            if (match != null) {
//                // Entry exists. Remove from entry map to prevent insert later.
//                entryMap.remove(entryId);
//                // Check to see if the entry needs to be updated
//                Uri existingUri = FeedContract.Entry.CONTENT_URI.buildUpon()
//                        .appendPath(Integer.toString(id)).build();
//                if ((match.title != null && !match.title.equals(title)) ||
//                        (match.link != null && !match.link.equals(link)) ||
//                        (match.published != published)) {
//                    // Update existing record
//                    Log.i(TAG, "Scheduling update: " + existingUri);
//                    batch.add(ContentProviderOperation.newUpdate(existingUri)
//                            .withValue(FeedContract.Entry.COLUMN_NAME_TITLE, title)
//                            .withValue(FeedContract.Entry.COLUMN_NAME_LINK, link)
//                            .withValue(FeedContract.Entry.COLUMN_NAME_PUBLISHED, published)
//                            .build());
//                    syncResult.stats.numUpdates++;
//                } else {
//                    Log.i(TAG, "No action: " + existingUri);
//                }
//            } else {
//                // Entry doesn't exist. Remove it from the database.
//                Uri deleteUri = FeedContract.Entry.CONTENT_URI.buildUpon()
//                        .appendPath(Integer.toString(id)).build();
//                Log.i(TAG, "Scheduling delete: " + deleteUri);
//                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
//                syncResult.stats.numDeletes++;
//            }
//        }
//        c.close();
//
//        // Add new items
//        for (FeedParser.Entry e : entryMap.values()) {
//            Log.i(TAG, "Scheduling insert: entry_id=" + e.id);
//            batch.add(ContentProviderOperation.newInsert(FeedContract.Entry.CONTENT_URI)
//                    .withValue(FeedContract.Entry.COLUMN_NAME_ENTRY_ID, e.id)
//                    .withValue(FeedContract.Entry.COLUMN_NAME_TITLE, e.title)
//                    .withValue(FeedContract.Entry.COLUMN_NAME_LINK, e.link)
//                    .withValue(FeedContract.Entry.COLUMN_NAME_PUBLISHED, e.published)
//                    .build());
//            syncResult.stats.numInserts++;
//        }
//        Log.i(TAG, "Merge solution ready. Applying batch update");
//        mContentResolver.applyBatch(FeedContract.CONTENT_AUTHORITY, batch);
//        mContentResolver.notifyChange(
//                FeedContract.Entry.CONTENT_URI, // URI where data was modified
//                null,                           // No local observer
//                false);                         // IMPORTANT: Do not sync to network
//        // This sample doesn't support uploads, but if *your* code does, make sure you set
//        // syncToNetwork=false in the line above to prevent duplicate syncs.
//    }
//
//    /**
//     * Given a string representation of a URL, sets up a connection and gets an input stream.
//     */
//    private InputStream downloadUrl(final URL url) throws IOException {
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS /* milliseconds */);
//        conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
//        conn.setRequestMethod("GET");
//        conn.setDoInput(true);
//        // Starts the query
//        conn.connect();
//        return conn.getInputStream();
//    }
}
