package account;

import java.io.*;

public class Account {

    public boolean createAccount(User user)
    {
        if(user.getName().equals("") || user.getAccountNumber().equals("") || user.getPassword().equals(""))
            return false;

        File folder = new File("src/resources/");
        File[] files = folder.listFiles();
        for(File f: files){
            if(f.getName().equals(user.getAccountNumber()))
                return false;
        }

        try {
            FileOutputStream fout = new FileOutputStream("src/resources/" + user.getAccountNumber());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fout);
            objectOutputStream.writeObject(user);

            objectOutputStream.close();
            fout.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean login(String accountNumber, String password)
    {
        File folder = new File("src/resources/");
        File[] files = folder.listFiles();
        for(File f: files){
            if(f.getName().equals(accountNumber))
            {
                try {
                    FileInputStream fin = new FileInputStream(f);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fin);

                    User user = (User) objectInputStream.readObject();

                    if(user.getPassword().equals(password))
                        return true;
                    else
                        return false;


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public boolean withdraw(String tmpammount, String accountNumber)
    {
        double ammount = Double.parseDouble(tmpammount);

        if(ammount<0)
            return false;


        File folder = new File("src/resources/");
        File[] files = folder.listFiles();
        for(File f: files){
            if(f.getName().equals(accountNumber))
            {
                try {
                    FileInputStream fin = new FileInputStream(f);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fin);

                    User user = (User) objectInputStream.readObject();

                    if(ammount >= user.getBalance())
                        return false;

                    user.setBalance(user.getBalance()-ammount);

                    FileOutputStream fout = new FileOutputStream(f);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fout);

                    objectOutputStream.writeObject(user);

                    fin.close();
                    objectInputStream.close();
                    fout.close();
                    objectOutputStream.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return true;
            }
        }

        return false;

    }


    public boolean deposit(String tmpammount, String accountNumber)
    {
        double ammount = Double.parseDouble(tmpammount);

        if(ammount<0)
            return false;


        File folder = new File("src/resources/");
        File[] files = folder.listFiles();
        for(File f: files){
            if(f.getName().equals(accountNumber))
            {
                try {
                    FileInputStream fin = new FileInputStream(f);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fin);

                    User user = (User) objectInputStream.readObject();

                    user.setBalance(user.getBalance()+ammount);

                    FileOutputStream fout = new FileOutputStream(f);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fout);

                    objectOutputStream.writeObject(user);

                    fin.close();
                    objectInputStream.close();
                    fout.close();
                    objectOutputStream.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return true;
            }
        }

        return false;

    }

    public boolean transferBalance(String from, String to, String tmpAmmount) throws IOException, ClassNotFoundException {
        File fromFile = null, toFile = null;
        double ammount = Double.parseDouble(tmpAmmount);

        if(ammount<=0)
            return false;

        File folder = new File("src/resources/");
        File[] files = folder.listFiles();

        for(File f: files)
        {
            if(f.getName().equals(from))
                fromFile = f;
            else if(f.getName().equals(to))
                toFile = f;
        }

        if(fromFile == null || toFile == null)
            return false;

        FileInputStream finFromFile = new FileInputStream(fromFile);
        ObjectInputStream objectInputStreamFromFile = new ObjectInputStream(finFromFile);

        User fromUser = (User) objectInputStreamFromFile.readObject();

        if(ammount >= fromUser.getBalance())
            return false;

        finFromFile.close();
        objectInputStreamFromFile.close();

        withdraw(tmpAmmount, from);
        deposit(tmpAmmount, to);

        return true;
    }

}
