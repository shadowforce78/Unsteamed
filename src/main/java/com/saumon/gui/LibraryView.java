package com.saumon.gui;

import com.saumon.core.model.Game;
import com.saumon.core.model.Studio;
import com.saumon.core.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Optional;

public class LibraryView {

    public Parent build() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + App.BG + ";");

        User user = App.getCurrentUser();

        // ── Header ─────────────────────────────────────────────────────────────
        HBox header = new HBox();
        header.setPadding(new Insets(24, 28, 16, 28));
        header.setStyle("-fx-border-color: transparent transparent #2a3d52 transparent; -fx-border-width: 0 0 1 0;");

        Label title = new Label("Ma Bibliotheque");
        title.setStyle("-fx-text-fill: " + App.TEXT + "; -fx-font-size: 22; -fx-font-weight: bold;");

        List<Integer> gameIds = user.getGames();
        Label count = new Label(gameIds.size() + " jeu(x)");
        count.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 13;");
        count.setMaxWidth(Double.MAX_VALUE);
        count.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(count, Priority.ALWAYS);

        header.getChildren().addAll(title, count);

        // ── Game list ──────────────────────────────────────────────────────────
        VBox list = new VBox(10);
        list.setPadding(new Insets(20, 28, 20, 28));

        if (gameIds.isEmpty()) {
            Label empty = new Label("Vous ne possedez aucun jeu pour l'instant.\nRendez-vous dans le catalogue pour acheter des jeux !");
            empty.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 14;");
            empty.setWrapText(true);
            list.getChildren().add(empty);
        } else {
            for (Integer gid : gameIds) {
                Game game = App.gameRepo.getGameById(gid);
                if (game != null) list.getChildren().add(buildGameCard(game, user));
            }
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

        // ── Left icon strip ─────────────────────────────────────────────────
        VBox strip = new VBox();
        strip.setPrefWidth(5);
        strip.setMinWidth(5);
        strip.setStyle("-fx-background-color: " + App.ACCENT + "; -fx-background-radius: 3;");

        // ── Info ─────────────────────────────────────────────────────────────
        VBox info = new VBox(5);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label name = new Label(game.getName());
        name.setStyle("-fx-text-fill: " + App.TEXT + "; -fx-font-size: 16; -fx-font-weight: bold;");

        Studio studio = App.studioRepo.getStudioById(game.getStudioID());
        String studioName = studio != null ? studio.getName() : "Studio inconnu";
        Label meta = new Label(studioName);
        meta.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 12;");

        info.getChildren().addAll(name, meta);

        // ── Right: level + update button ─────────────────────────────────────
        VBox right = new VBox(8);
        right.setAlignment(Pos.CENTER_RIGHT);
        right.setMinWidth(200);

        Integer lvl = user.getProgression().get(game.getId());
        Label levelLabel = new Label("Niveau : " + (lvl != null ? lvl : "Non commencé"));
        levelLabel.setStyle("-fx-text-fill: " + App.PRICE_CLR + "; -fx-font-size: 14; -fx-font-weight: bold;");

        Button updateBtn = new Button("Mettre a jour la progression");
        updateBtn.setPrefWidth(200);
        String btnStyle = "-fx-background-color: " + App.ACCENT + "; -fx-text-fill: white; "
                        + "-fx-font-size: 12; -fx-padding: 7 14; -fx-background-radius: 5; -fx-cursor: hand;";
        updateBtn.setStyle(btnStyle);
        updateBtn.setOnMouseEntered(e -> updateBtn.setStyle(btnStyle.replace(App.ACCENT, "#3ab0ff")));
        updateBtn.setOnMouseExited(e  -> updateBtn.setStyle(btnStyle));

        updateBtn.setOnAction(e -> {
            Optional<Integer> result = showProgressionDialog(game, lvl);
            result.ifPresent(newLevel -> {
                App.userRepo.updateUserProgression(user, game.getId(), newLevel);
                levelLabel.setText("Niveau : " + newLevel);
            });
        });

        right.getChildren().addAll(levelLabel, updateBtn);

        card.getChildren().addAll(strip, info, right);
        return card;
    }

    private Optional<Integer> showProgressionDialog(Game game, Integer current) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Progression");
        dialog.setHeaderText("Mettre a jour la progression pour :\n" + game.getName());

        ButtonType confirmBtn = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn  = new ButtonType("Annuler",   ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmBtn, cancelBtn);

        Spinner<Integer> spinner = new Spinner<>(0, 9999, current != null ? current : 0, 1);
        spinner.setEditable(true);
        spinner.setPrefWidth(130);
        spinner.setStyle("-fx-font-size: 14;");

        VBox content = new VBox(10,
                new Label("Niveau actuel : " + (current != null ? current : "Non commencé")),
                new Label("Nouveau niveau :"),
                spinner);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(btn -> {
            if (btn == confirmBtn) {
                spinner.commitValue();
                return spinner.getValue();
            }
            return null;
        });

        // Apply basic dark styling to dialog pane
        dialog.getDialogPane().setStyle("-fx-background-color: " + App.SIDEBAR + ";");

        return dialog.showAndWait();
    }
}

