package com.saumon.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class HelpView {

    private record Feature(String cmd, String label, String desc) {}

    public Parent build() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + App.BG + ";");

        // ── Header ────────────────────────────────────────────────────────────
        HBox header = new HBox();
        header.setPadding(new Insets(24, 28, 16, 28));
        header.setStyle("-fx-border-color: transparent transparent #2a3d52 transparent; -fx-border-width: 0 0 1 0;");

        Label title = new Label("Aide & Fonctionnalites");
        title.setStyle("-fx-text-fill: " + App.TEXT + "; -fx-font-size: 22; -fx-font-weight: bold;");
        header.getChildren().add(title);

        // ── Content ───────────────────────────────────────────────────────────
        VBox content = new VBox(16);
        content.setPadding(new Insets(24, 28, 28, 28));

        Label intro = new Label("Bienvenue sur Unsteamed ! Voici les fonctionnalites disponibles depuis la barre laterale :");
        intro.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 14;");
        intro.setWrapText(true);
        content.getChildren().add(intro);

        Feature[] features = {
            new Feature("--g", "Catalogue de jeux",  "Parcourez tous les jeux disponibles et achetez ceux qui vous interessent."),
            new Feature("--a", "Ma Bibliotheque",     "Consultez les jeux que vous possedez et mettez a jour votre progression."),
            new Feature("--c", "Creation de compte",  "Creez un nouveau compte avec un nom d'utilisateur, un email et un mot de passe."),
            new Feature("--i", "Connexion",           "Connectez-vous a votre compte existant avec votre email et mot de passe."),
            new Feature("--b", "Achat de jeu",        "Ajoutez un jeu a votre bibliotheque depuis la fiche du jeu dans le catalogue."),
            new Feature("--p", "Mise a jour progression", "Definissez votre niveau actuel sur un jeu de votre bibliotheque."),
        };

        for (Feature f : features) {
            content.getChildren().add(buildFeatureCard(f));
        }

        // ── CLI note ──────────────────────────────────────────────────────────
        VBox cliNote = new VBox(6);
        cliNote.setPadding(new Insets(16));
        cliNote.setStyle("-fx-background-color: #1e3a52; -fx-background-radius: 8;");

        Label cliTitle = new Label("Mode CLI");
        cliTitle.setStyle("-fx-text-fill: " + App.ACCENT + "; -fx-font-size: 14; -fx-font-weight: bold;");

        Label cliDesc = new Label(
            "L'application supporte egalement un mode ligne de commande :\n" +
            "  --c <username> <email> <password>   Creation de compte\n" +
            "  --i <email> <password>              Connexion\n" +
            "  --a                                 Bibliotheque\n" +
            "  --g                                 Catalogue\n" +
            "  --b <gameId>                        Achat (ex: --b 1,2,3)\n" +
            "  --p <gameId> <level>                Progression\n" +
            "  --h                                 Aide"
        );
        cliDesc.setStyle("-fx-text-fill: " + App.TEXT + "; -fx-font-size: 12; -fx-font-family: monospace;");
        cliNote.getChildren().addAll(cliTitle, cliDesc);
        content.getChildren().add(cliNote);

        // ── About ─────────────────────────────────────────────────────────────
        Label about = new Label("Unsteamed v1.0 — Projet Java / JavaFX");
        about.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 12;");
        content.getChildren().add(about);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + App.BG + "; -fx-background: " + App.BG + ";");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        root.getChildren().addAll(header, scroll);
        return root;
    }

    private HBox buildFeatureCard(Feature feature) {
        HBox card = new HBox(16);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setStyle("-fx-background-color: " + App.CARD + "; -fx-background-radius: 8;");
        card.setAlignment(Pos.CENTER_LEFT);

        // CLI tag badge
        Label tag = new Label(feature.cmd());
        tag.setMinWidth(70);
        tag.setAlignment(Pos.CENTER);
        tag.setStyle("-fx-background-color: #1e3a52; -fx-text-fill: " + App.ACCENT
                   + "; -fx-font-size: 12; -fx-font-family: monospace; -fx-padding: 4 10; -fx-background-radius: 5;");

        // Description
        VBox info = new VBox(3);
        Label name = new Label(feature.label());
        name.setStyle("-fx-text-fill: " + App.TEXT + "; -fx-font-size: 14; -fx-font-weight: bold;");
        Label desc = new Label(feature.desc());
        desc.setStyle("-fx-text-fill: " + App.DIM + "; -fx-font-size: 12;");
        desc.setWrapText(true);
        info.getChildren().addAll(name, desc);
        HBox.setHgrow(info, Priority.ALWAYS);

        card.getChildren().addAll(tag, info);
        return card;
    }
}

