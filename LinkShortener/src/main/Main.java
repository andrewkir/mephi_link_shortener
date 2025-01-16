package main;

import main.models.Config;
import main.models.LinkModel;
import main.models.User;
import main.models.Users;
import main.utils.ConfigLoader;
import main.utils.LinkShortener;
import main.utils.LinksManager;
import main.utils.UsersManager;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        User user = null;
        Users users = UsersManager.loadUsers();
        Scanner scanner = new Scanner(System.in);
        LinkShortener shortener = new LinkShortener();

        while (true) {
            System.out.println("Для начала работы с сервисом нужно войти или зарегистрироваться");
            System.out.println("1 - Вход");
            System.out.println("2 - Регистрация");
            System.out.println("Введите 3 для завершения работы");

            String next = scanner.nextLine();
            switch (next) {
                case "1":
                    System.out.print("Введите userId: ");
                    String enteredUserId = scanner.nextLine();
                    for (User localUser : users.getUsers()) {
                        if (enteredUserId.equals(localUser.getUserId())) {
                            user = localUser;
                            break;
                        }
                    }
                    break;
                case "2":
                    String userId = UUID.randomUUID().toString();
                    user = new User(userId);
                    UsersManager.saveUser(user, users);
                    System.out.println("Ваш userId: " + userId + "\nСохраните его для входа!");
                    break;

                case "3":
                    return;

                default:
                    System.out.println("Введите корректную команду");
                    continue;
            }

            if (user == null) {
                System.out.println("Не удалось войти, повторите попытку");
            } else {
                break;
            }
        }

        System.out.println("Успешный вход!");

        while (true) {
            System.out.println("Доступные команды:");
            System.out.println("1 - Просмотр списка коротких ссылок");
            System.out.println("2 - Создание короткой ссылки");
            System.out.println("3 - Переход по короткой ссылке");
            System.out.println("4 - Изменение лимита переходов");
            System.out.println("5 - Удаление короткой ссылки");
            System.out.println("6 - Завершить работу");

            String next = scanner.nextLine();
            switch (next) {
                case "1":
                    handleLinksList(user);
                    break;
                case "2":
                    handleCreateLink(user, shortener);
                    break;
                case "3":
                    handleLinkRedirect(shortener);
                    break;
                case "4":
                    handleLinkClicksEdit(user, shortener);
                    break;
                case "5":
                    handleLinkDeletion(user, shortener);
                    break;
                case "6":
                    return;
            }
        }
    }


    private static void handleLinksList(User user) {
        List<LinkModel> links = LinksManager.getLinksForUser(user.getUserId());
        if (links.isEmpty()) {
            System.out.println("У вас пока нет коротких ссылок");
        }
        for (LinkModel link : links) {
            System.out.println(link.getShortenedUrl() + " - " + link.getOriginalUrl());
        }
    }

    private static void handleCreateLink(User user, LinkShortener shortener) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ссылку, которую нужно сократить:");
        String link = scanner.nextLine();
        Config config = ConfigLoader.loadConfig();

        System.out.println("Введите лимит переходов или 0, для значения по умолчанию:");
        int clickLimit = 0;
        try {
            clickLimit = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Введен неверный формат, использую значение по умолчанию");
        }
        scanner.nextLine();

        if (clickLimit == 0) {
            clickLimit = config.getClickLimit();
        }

        System.out.println("Введите время жизни в часах или 0, для значения по умолчанию:");
        int hoursAlive = 0;
        try {
            hoursAlive = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Введен неверный формат, использую значение по умолчанию");
        }
        scanner.nextLine();

        if (hoursAlive == 0) {
            hoursAlive = config.getLinkHours();
        }

        String result = shortener.getShortLink(user.getUserId(), link, clickLimit, hoursAlive);
        System.out.println("Ваша короткая ссылка:\n" + result);
    }

    private static void handleLinkRedirect(LinkShortener shortener){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ссылку:");
        String shortLink = scanner.nextLine();
        List<LinkModel> links = LinksManager.getLinks();
        for (LinkModel link: links) {
            if (link.getShortenedUrl().equals(shortLink)) {
                shortener.linkRedirect(link);
            }
        }
    }

    private static void handleLinkClicksEdit(User user, LinkShortener shortener) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите короткую ссылку, в которой надо изменить лимит переходов:");
        String enteredLink = scanner.nextLine();
        List<LinkModel> links = LinksManager.getLinksForUser(user.getUserId());
        for (LinkModel link: links) {
            if (link.getShortenedUrl().equals(enteredLink)) {
                int clickLimit = ConfigLoader.loadConfig().getClickLimit();
                try {
                    System.out.println("Введите новый лимит переходов:");
                    clickLimit = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Введен неверный формат, использую значение по умолчанию");
                }
                scanner.nextLine();
                shortener.updateLinkClicksLimit(link, clickLimit);
                System.out.println("Успешно!");
                return;
            }
        }
        System.out.println("Не удалось найти ссылку");
    }

    private static void handleLinkDeletion(User user, LinkShortener shortener) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите короткую ссылку, которую нужно удалить");
        String enteredLink = scanner.nextLine();
        List<LinkModel> links = LinksManager.getLinksForUser(user.getUserId());
        for (LinkModel link: links) {
            if (link.getShortenedUrl().equals(enteredLink)) {
                shortener.deleteLink(link);
                System.out.println("Успешно!");
                return;
            }
        }
        System.out.println("Не удалось найти ссылку, либо Вы не являетесь создателем");
    }
}