import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookID;
    private String title;
    private String author;
    private String genre;
    private boolean availability;

    public Book(int bookID, String title, String author, String genre) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availability = true;
    }

    public int getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Book ID: " + bookID + ", Title: " + title + ", Author: " + author + ", Genre: " + genre
                + ", Available: " + availability;
    }
}

class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userID;
    private String name;
    private String contactInfo;
    private ArrayList<Integer> borrowedBooks;

    public User(int userID, String name, String contactInfo) {
        this.userID = userID;
        this.name = name;
        this.contactInfo = contactInfo;
        this.borrowedBooks = new ArrayList<>();
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public ArrayList<Integer> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(int bookID) {
        borrowedBooks.add(bookID);
    }

    public void returnBook(int bookID) {
        borrowedBooks.remove(Integer.valueOf(bookID));
    }

    @Override
    public String toString() {
        return "User ID: " + userID + ", Name: " + name + ", Contact Info: " + contactInfo + ", Borrowed Books: "
                + borrowedBooks;
    }
}

class Library implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Book> books;
    private ArrayList<User> users;

    public Library() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    public Book findBookByAuthor(String author) {
        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                return book;
            }
        }
        return null;
    }

    public User findUserByID(int userID) {
        for (User user : users) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        return null;
    }

    public void displayBooks() {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void displayUsers() {
        for (User user : users) {
            System.out.println(user);
        }
    }

    public void saveDataToFile() {
        try (ObjectOutputStream bookStream = new ObjectOutputStream(new FileOutputStream("books.dat"));
                ObjectOutputStream userStream = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            bookStream.writeObject(books);
            userStream.writeObject(users);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadDataFromFile() {
        try (ObjectInputStream bookStream = new ObjectInputStream(new FileInputStream("books.dat"));
                ObjectInputStream userStream = new ObjectInputStream(new FileInputStream("users.dat"))) {
            books = (ArrayList<Book>) bookStream.readObject();
            users = (ArrayList<User>) userStream.readObject();
            System.out.println("Data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}

public class LibraryManagementSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        library.loadDataFromFile();

        int choice;
        do {
            System.out.println("Library Management System Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Add User");
            System.out.println("3. Display Books");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Search Books by Title or Author");
            System.out.println("7. Display Users");
            System.out.println("8. Save and Exit");
            System.out.print("Enter your choice (1-8): ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addBook(library, scanner);
                    break;
                case 2:
                    addUser(library, scanner);
                    break;
                case 3:
                    library.displayBooks();
                    break;
                case 4:
                    borrowBook(library, scanner);
                    break;
                case 5:
                    returnBook(library, scanner);
                    break;
                case 6:
                    searchBooks(library, scanner);
                    break;
                case 7:
                    library.displayUsers();
                    break;
                case 8:
                    library.saveDataToFile();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 8.");
            }
        } while (choice != 8);
    }

    private static void addBook(Library library, Scanner scanner) {
        System.out.print("Enter Book ID: ");
        int bookID = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        Book book = new Book(bookID, title, author, genre);
        library.addBook(book);
        System.out.println("Book added successfully.");
    }

    private static void addUser(Library library, Scanner scanner) {
        System.out.print("Enter User ID: ");
        int userID = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Contact Information: ");
        String contactInfo = scanner.nextLine();

        User user = new User(userID, name, contactInfo);
        library.addUser(user);
        System.out.println("User added successfully.");
    }

    private static void borrowBook(Library library, Scanner scanner) {
        System.out.print("Enter User ID: ");
        int userID = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter Book Title to Borrow: ");
        String bookTitleToBorrow = scanner.nextLine();

        User user = library.findUserByID(userID);
        Book book = library.findBookByTitle(bookTitleToBorrow);

        if (user != null && book != null && book.isAvailable()) {
            user.borrowBook(book.getBookID());
            book.setAvailability(false);
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Invalid User ID or Book Title, or the book is not available.");
        }
    }

    private static void returnBook(Library library, Scanner scanner) {
        System.out.print("Enter User ID: ");
        int userID = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter Book Title to Return: ");
        String bookTitleToReturn = scanner.nextLine();

        User user = library.findUserByID(userID);
        Book book = library.findBookByTitle(bookTitleToReturn);

        if (user != null && book != null && user.getBorrowedBooks().contains(book.getBookID())) {
            user.returnBook(book.getBookID());
            book.setAvailability(true);
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Invalid User ID or Book Title, or the book is not borrowed by the user.");
        }
    }

    private static void searchBooks(Library library, Scanner scanner) {
        System.out.println("Search Books by:");
        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.print("Enter your choice (1 or 2): ");
        int searchChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (searchChoice) {
            case 1:
                System.out.print("Enter Title to Search: ");
                String title = scanner.nextLine();
                Book foundBookByTitle = library.findBookByTitle(title);
                if (foundBookByTitle != null) {
                    System.out.println(foundBookByTitle);
                } else {
                    System.out.println("Book not found.");
                }
                break;
            case 2:
                System.out.print("Enter Author to Search: ");
                String author = scanner.nextLine();
                Book foundBookByAuthor = library.findBookByAuthor(author);
                if (foundBookByAuthor != null) {
                    System.out.println(foundBookByAuthor);
                } else {
                    System.out.println("Book not found.");
                }
                break;
            default:
                System.out.println("Invalid choice. Please enter 1 or 2.");
        }
    }
}
