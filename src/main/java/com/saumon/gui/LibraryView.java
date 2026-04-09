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
        Label levelLabel = new Label("Progression : " + (lvl != null ? lvl : "0") + " %");
        levelLabel.setStyle("-fx-text-fill: " + App.PRICE_CLR + "; -fx-font-size: 14; -fx-font-weight: bold;");

        Double hours = user.getPlaytime().getOrDefault(game.getId(), 0.0);
        Label playtimeLabel = new Label("Temps de jeu : " + hours + " h");
        playtimeLabel.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 12;");

        Button updateBtn = new Button("Mettre à jour");
        updateBtn.setPrefWidth(200);
        String btnStyle = "-fx-background-color: " + App.ACCENT + "; -fx-text-fill: white; "
                        + "-fx-font-size: 12; -fx-padding: 7 14; -fx-background-radius: 5; -fx-cursor: hand;";
        updateBtn.setStyle(btnStyle);
        updateBtn.setOnMouseEntered(e -> updateBtn.setStyle(btnStyle.replace(App.ACCENT, "#3ab0ff")));
        updateBtn.setOnMouseExited(e  -> updateBtn.setStyle(btnStyle));

        updateBtn.setOnAction(e -> {
            showUpdateDialog(game, lvl, hours).ifPresent(result -> {
                int newLevel = result.getKey();
                double newHours = result.getValue();
                App.userRepo.updateUserProgression(user, game.getId(), newLevel);
                App.userRepo.updateUserPlaytime(user, game.getId(), newHours);
                levelLabel.setText("Progression : " + newLevel + " %");
                playtimeLabel.setText("Temps de jeu : " + newHours + " h");
            });
        });

        Button refundBtn = new Button("Rembourser");
        refundBtn.setPrefWidth(200);
        String refundBtnStyle = "-fx-background-color: transparent; -fx-border-color: #ff6b6b; -fx-text-fill: #ff6b6b; "
                              + "-fx-font-size: 12; -fx-padding: 6 13; -fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand;";
        refundBtn.setStyle(refundBtnStyle);
        refundBtn.setOnMouseEntered(e -> refundBtn.setStyle(refundBtnStyle.replace("transparent", "#2a1e1e")));
        refundBtn.setOnMouseExited(e  -> refundBtn.setStyle(refundBtnStyle));
        
        refundBtn.setOnAction(e -> {
            boolean success = App.userRepo.refundGame(user, game);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Jeu remboursé avec succès.");
                alert.showAndWait();

                if (App.getMainView() != null) {
                    App.getMainView().refreshBalance();
                    App.getMainView().refreshLibraryView();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible de rembourser (temps de jeu >= 2h).");
                alert.show();
            }
        });

        right.getChildren().addAll(levelLabel, playtimeLabel, updateBtn, refundBtn);

        card.getChildren().addAll(strip, info, right);
        return card;
    }

    private Optional<javafx.util.Pair<Integer, Double>> showUpdateDialog(Game game, Integer currentLevel, Double currentHours) {
        Dialog<javafx.util.Pair<Integer, Double>> dialog = new Dialog<>();
        dialog.setTitle("Mettre à jour");
        dialog.setHeaderText("Progression & Temps de jeu :\n" + game.getName());

        ButtonType confirmBtn = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn  = new ButtonType("Annuler",   ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmBtn, cancelBtn);

        Spinner<Integer> lvlSpinner = new Spinner<>(0, 100, currentLevel != null ? currentLevel : 0, 1);
        lvlSpinner.setEditable(true);
        lvlSpinner.setPrefWidth(130);

        Spinner<Double> hoursSpinner = new Spinner<>(0.0, 9999.0, currentHours != null ? currentHours : 0.0, 0.5);
        hoursSpinner.setEditable(true);
        hoursSpinner.setPrefWidth(130);

        VBox content = new VBox(10,
                new Label("Progression (%) :"), lvlSpinner,
                new Label("Temps de jeu (h) :"), hoursSpinner);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(btn -> {
            if (btn == confirmBtn) {
                lvlSpinner.commitValue();
                hoursSpinner.commitValue();
                return new javafx.util.Pair<>(lvlSpinner.getValue(), hoursSpinner.getValue());
            }
            return null;
        });

        dialog.getDialogPane().setStyle("-fx-background-color: " + App.SIDEBAR + ";");

        return dialog.showAndWait();
    }
}
