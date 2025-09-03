package user;

public class User
{
    private String username;
    private String email;

    static { System.out.println("User class - Loaded into memory"); }

    public User(String username, String email)
    {
        this.username = username;
        this.email = email;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }

    @Override
    protected void finalize() throws Throwable
    {
        System.out.println("[Finalizer] User removed: " + username);
        super.finalize();
    }

    @Override
    public String toString() { return username + " <" + email + ">"; }
}