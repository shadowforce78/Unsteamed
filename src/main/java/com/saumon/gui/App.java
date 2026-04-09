package com.saumon.gui;

import com.saumon.core.model.User;
import com.saumon.core.repository.GameRepository;
import com.saumon.core.repository.StudioRepository;
import com.saumon.core.repository.UserRepository;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;
    private static User currentUser;

    public static final UserRepository userRepo = new UserRepository();
    public static final GameRepository gameRepo = new GameRepository();
    public static final StudioRepository studioRepo = new StudioRepository();

    // ── Palette ──────────────────────────────────────────────────────────────
    public static final String BG        = "#1b2838";
    public static final String SIDEBAR   = "#171a21";
    public static final String CARD      = "#2a475e";
    public static final String CARD_HVR  = "#1e3a52";
    public static final String ACCENT    = "#1a9fff";
    public static final String TEXT      = "#c7d5e0";
    public static final String DIM       = "#8f98a0";
    public static final String PRICE_CLR = "#beee11";
    public static final String BUY_CLR   = "#4c7a1e";
    public static final String BUY_HVR   = "#5c9422";
    public static final String OWNED_CLR = "#3a3d40";

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("Unsteamed");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setWidth(1280);
        stage.setHeight(800);
        showLogin();
        stage.show();
    }

    public static void showLogin() {
        Scene scene = new Scene(new LoginView().build(),
                primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(scene);
    }

    private static MainView currentMainView;

    public static void showMain() {
        currentMainView = new MainView();
        Scene scene = new Scene(currentMainView.build(),
                primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(scene);
    }

    public static MainView getMainView() {
        return currentMainView;
    }

    public static User getCurrentUser()          { return currentUser; }
    public static void setCurrentUser(User user) { currentUser = user; }

    public static void logout() {
        currentUser = null;
        showLogin();
    }
}
