package com.saumon.gui;

import com.saumon.core.model.Game;
import com.saumon.core.model.Studio;
import com.saumon.core.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.text.SimpleDateFormat;
import java.util.List;

public class CatalogView {

    public Parent build() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + App.BG + ";");

        // ── Header ────────────────────────────────────────────────────────────
        HBox header = new HBox();
        header.setPadding(new Insets(24, 28, 16, 28));
        header.setStyle("-fx-border-color: transparent transparent #2a3d52 transparent; -fx-border-width: 0 0 1 0;");

        Label title = new Label("Catalogue de jeux");
        title.setStyle("-fx-text-fill: " + App.TEXT + "; -fx-font-size: 22; -fx-font-weight: bold;");

        List<Game> games = App.gameRepo.getAllGames();
        Label count = new Label(games.size() + " jeux disponibles");
        count.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 13;");
        count.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(count, Priority.ALWAYS);
        count.setMaxWidth(Double.MAX_VALUE);
        count.setTextAlignment(TextAlignment.RIGHT);

        header.getChildren().addAll(title, count);

        // ── Game list ─────────────────────────────────────────────────────────
        VBox list = new VBox(10);
        list.setPadding(new Insets(20, 28, 20, 28));

        User user = App.getCurrentUser();

        for (Game game : games) {
            list.getChildren().add(buildGameCard(game, user));
        }

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + App.BG + "; -fx-background: " + App.BG + ";");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        root.getChildren().addAll(header, scroll);
        return root;
    }

    private HBox buildGameCard(Game game, User user) {
        HBox card = new HBox(16);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setStyle("-fx-background-color: " + App.CARD + "; -fx-background-radius: 8;");
        card.setAlignment(Pos.CENTER_LEFT);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: " + App.CARD_HVR + "; -fx-background-radius: 8;"));
        card.setOnMouseExited(e  -> card.setStyle("-fx-background-color: " + App.CARD   + "; -fx-background-radius: 8;"));

        // ── Left: colored genre strip ──────────────────────────────────────────
        VBox colorBar = new VBox();
        colorBar.setPrefWidth(5);
        colorBar.setMinWidth(5);
        colorBar.setStyle("-fx-background-color: " + genreColor(game.getGenres()) + "; -fx-background-radius: 3;");

        // ── Center: info ──────────────────────────────────────────────────────
        VBox info = new VBox(6);
        HBox.setHgrow(info, Priority.ALWAYS);

        // Name
        Label name = new Label(game.getName());
        name.setStyle("-fx-text-fill: " + App.TEXT + "; -fx-font-size: 16; -fx-font-weight: bold;");

        // Studio + date + multiplayer
        Studio studio = App.studioRepo.getStudioById(game.getStudioID());
        String studioName = studio != null ? studio.getName() : "Studio inconnu";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = game.getReleaseDate() != null ? sdf.format(game.getReleaseDate()) : "?";
        String multi = Boolean.TRUE.equals(game.getIsMultiplayer()) ? "  •  Multijoueur" : "";
        String sub   = Boolean.TRUE.equals(game.getSubscription())  ? "  •  Abonnement" : "";

        Label meta = new Label(studioName + "  •  " + year + multi + sub);
        meta.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 12;");

        // Genres badges
        HBox badges = new HBox(6);
        if (game.getGenres() != null) {
            for (String genre : game.getGenres()) {
                Label badge = new Label(genre);
                badge.setStyle("-fx-background-color: #1e3a52; -fx-text-fill: " + App.ACCENT
                             + "; -fx-font-size: 11; -fx-padding: 2 8; -fx-background-radius: 10;");
                badges.getChildren().add(badge);
            }
        }

        // Description
        Label desc = new Label(game.getDescription() != null ? game.getDescription() : "");
        desc.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 12;");
        desc.setWrapText(true);
        desc.setMaxWidth(600);

        info.getChildren().addAll(name, meta, badges, desc);

        // ── Right: price + buy button ─────────────────────────────────────────
        VBox right = new VBox(10);
        right.setAlignment(Pos.CENTER_RIGHT);
        right.setMinWidth(130);

        Label priceLabel = new Label(game.getPrice() != null ? String.format("$%.2f", game.getPrice()) : "Gratuit");
        priceLabel.setStyle("-fx-text-fill: " + App.PRICE_CLR + "; -fx-font-size: 18; -fx-font-weight: bold;");

        boolean owned = user != null && user.getGames().contains(game.getId());

        Button buyBtn = new Button(owned ? "Possede" : "Acheter");
        buyBtn.setPrefWidth(120);
        applyBuyStyle(buyBtn, owned);

        if (!owned && user != null) {
            buyBtn.setOnAction(e -> {
                App.userRepo.addGameToUser(user, game);
                buyBtn.setText("Possede");
                applyBuyStyle(buyBtn, true);
                buyBtn.setOnAction(null);
            });
        }

        right.getChildren().addAll(priceLabel, buyBtn);

        card.getChildren().addAll(colorBar, info, right);
        return card;
    }

    private void applyBuyStyle(Button btn, boolean owned) {
        if (owned) {
            btn.setStyle("-fx-background-color: " + App.OWNED_CLR + "; -fx-text-fill: " + App.DIM
                       + "; -fx-font-size: 13; -fx-padding: 8 16; -fx-background-radius: 5; -fx-cursor: default;");
            btn.setDisable(true);
        } else {
            String s = "-fx-background-color: " + App.BUY_CLR + "; -fx-text-fill: white; "
                     + "-fx-font-size: 13; -fx-font-weight: bold; -fx-padding: 8 16; "
                     + "-fx-background-radius: 5; -fx-cursor: hand;";
            btn.setStyle(s);
            btn.setOnMouseEntered(e -> btn.setStyle(s.replace(App.BUY_CLR, App.BUY_HVR)));
            btn.setOnMouseExited(e  -> btn.setStyle(s));
        }
    }

    /** Returns a distinct color per primary genre */
    private String genreColor(List<String> genres) {
        if (genres == null || genres.isEmpty()) return App.DIM;
        return switch (genres.get(0).toLowerCase()) {
            case "rpg"        -> "#c77e00";
            case "action"     -> "#c03030";
            case "adventure"  -> "#2e8b57";
            case "open world" -> "#1e7a6a";
            case "fps"        -> "#8b2252";
            case "strategy"   -> "#4a6fa5";
            case "sports"     -> "#2277bb";
            case "simulation" -> "#7a5c2e";
            case "horror"     -> "#4d1f5e";
            case "racing"     -> "#c7600a";
            default           -> App.ACCENT;
        };
    }
}

