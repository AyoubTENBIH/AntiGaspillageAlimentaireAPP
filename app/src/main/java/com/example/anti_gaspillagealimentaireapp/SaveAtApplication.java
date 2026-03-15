package com.example.anti_gaspillagealimentaireapp;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.example.anti_gaspillagealimentaireapp.data.local.AppDatabase;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Commerce;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Panier;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.Reservation;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.ReservationStatut;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.User;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.UserRole;
import com.example.anti_gaspillagealimentaireapp.utils.HashUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Point d'entrée Application SaveAt — initialise la DB et le seeding au premier lancement.
 */
public class SaveAtApplication extends Application {

    private static final String TAG = "SaveAtDebug";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private AppDatabase database;

    public AppDatabase getDatabase() {
        if (database == null) {
            database = AppDatabase.getInstance(this);
        }
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        installCrashLogger();
        Log.d(TAG, "[1/1] Application.onCreate() démarré");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "[DB] Accès database (init)...");
                    getDatabase().userDao().getByEmail("");
                    Log.d(TAG, "[DB] Init OK");
                } catch (Exception e) {
                    Log.e(TAG, "[DB] Init DB erreur", e);
                }
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "[SEED] Début seedIfNeeded()");
                    seedIfNeeded();
                    Log.d(TAG, "[SEED] Fin seedIfNeeded() OK");
                } catch (Exception e) {
                    Log.e(TAG, "[SEED] Erreur seeding", e);
                }
            }
        });
        Log.d(TAG, "[1/1] Application.onCreate() terminé");
    }

    private void installCrashLogger() {
        final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Log.e(TAG, "========== CRASH SaveAt ==========");
                Log.e(TAG, "Thread: " + thread.getName(), throwable);
                throwable.printStackTrace();
                if (defaultHandler != null) {
                    defaultHandler.uncaughtException(thread, throwable);
                }
            }
        });
    }

    private void seedIfNeeded() {
        try {
            if (getDatabase().userDao().getByEmail("client@test.com") != null) return;

            String hashClient = HashUtils.md5("password123");
            String hashShop = HashUtils.md5("password123");

            User client = new User("Jean Client", "client@test.com", hashClient, UserRole.CLIENT, System.currentTimeMillis());
            long clientId = getDatabase().userDao().insert(client);

            User commerçant1 = new User("Marie Boulangère", "shop@test.com", hashShop, UserRole.COMMERCANT, System.currentTimeMillis());
            User commerçant2 = new User("Paul Restaurateur", "paul@test.com", hashShop, UserRole.COMMERCANT, System.currentTimeMillis());
            long id1 = getDatabase().userDao().insert(commerçant1);
            long id2 = getDatabase().userDao().insert(commerçant2);

            Commerce c1 = new Commerce(0, "Au Pain Doré", "12 rue de la Paix, Naperville",
                "Boulangerie artisanale, viennoiseries et pains du jour.",
                "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400&q=80", id1, "Boulangerie", true, 41.7520, -88.1550);
            Commerce c2 = new Commerce(0, "La Petite Épicerie", "5 avenue des Champs, Naperville",
                "Épicerie de quartier, produits frais et locaux.",
                "https://images.unsplash.com/photo-1534723452862-4c874018d66d?w=400&q=80", id1, "Épicerie", true, 41.7480, -88.1480);
            Commerce c3 = new Commerce(0, "Le Bistrot du Marché", "8 place du Marché, Naperville",
                "Cuisine du marché, plats du jour et invendus du soir.",
                "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=400&q=80", id2, "Restaurant", true, 41.7510, -88.1520);
            Commerce c4 = new Commerce(0, "Pizza & Co", "22 rue de la Gare, Naperville",
                "Pizzeria et plats à emporter.",
                "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400&q=80", id2, "Restaurant", true, 41.7495, -88.1570);

            long idC1 = getDatabase().commerceDao().insert(c1);
            long idC2 = getDatabase().commerceDao().insert(c2);
            long idC3 = getDatabase().commerceDao().insert(c3);
            long idC4 = getDatabase().commerceDao().insert(c4);

            long now = System.currentTimeMillis();
            long in24h = now + 24 * 60 * 60 * 1000;
            long in48h = now + 48 * 60 * 60 * 1000;

            Panier[] paniers = new Panier[] {
                new Panier(0, "Panier du matin", "Croissants, pains au chocolat et baguettes du jour. Idéal pour le petit-déjeuner.", 3.50, 5, idC1, in24h, "https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=400&q=80", true),
                new Panier(0, "Viennoiseries invendues", "Assortiment de viennoiseries encore fraîches, à consommer rapidement.", 2.00, 3, idC1, in24h, "https://images.unsplash.com/photo-1509365465985-25d11c17e812?w=400&q=80", true),
                new Panier(0, "Pain de campagne", "Gros pain de campagne et ficelles. Parfait pour les repas en famille.", 1.50, 8, idC1, in48h, "https://images.unsplash.com/photo-1549931319-a1c447a1e0f4?w=400&q=80", true),
                new Panier(0, "Fruits et légumes du jour", "Surplus du marché : pommes, carottes, salade. Fraîcheur garantie.", 4.00, 4, idC2, in24h, "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=400&q=80", true),
                new Panier(0, "Fromages à date courte", "Sélection de fromages à consommer sous 48h. Idéal pour un plateau.", 6.00, 2, idC2, in48h, "https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=400&q=80", true),
                new Panier(0, "Plat du jour invendu", "Quiche lorraine et salade. Préparé ce midi, à réchauffer.", 5.50, 3, idC3, in24h, "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=400&q=80", true),
                new Panier(0, "Soupe et dessert", "Soupe du jour + part de tarte aux pommes. Évitez le gaspillage.", 4.50, 6, idC3, in24h, "https://images.unsplash.com/photo-1547592166-23ac45744acd?w=400&q=80", true),
                new Panier(0, "Pizzas du soir", "Pizzas margherita et 4 fromages. À réchauffer au four.", 7.00, 4, idC4, in24h, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400&q=80", true),
            };
            long[] panierIds = new long[paniers.length];
            for (int i = 0; i < paniers.length; i++) {
                panierIds[i] = getDatabase().panierDao().insert(paniers[i]);
            }
            long nowRes = System.currentTimeMillis();
            long yesterday = nowRes - 24 * 60 * 60 * 1000L;
            Reservation[] reservations = new Reservation[] {
                new Reservation(0, clientId, panierIds[0], nowRes, ReservationStatut.EN_ATTENTE),
                new Reservation(0, clientId, panierIds[3], nowRes - 3600000L, ReservationStatut.EN_ATTENTE),
                new Reservation(0, clientId, panierIds[2], yesterday, ReservationStatut.CONFIRMEE),
                new Reservation(0, clientId, panierIds[4], yesterday - 86400000L, ReservationStatut.CONFIRMEE),
                new Reservation(0, clientId, panierIds[5], yesterday - 172800000L, ReservationStatut.CONFIRMEE),
                new Reservation(0, clientId, panierIds[1], nowRes - 259200000L, ReservationStatut.ANNULEE),
            };
            for (Reservation r : reservations) {
                getDatabase().reservationDao().insert(r);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur seedIfNeeded", e);
        }
    }
}
