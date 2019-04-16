package server;

import account.Account;
import account.User;

import java.io.*;
import java.net.Socket;

public class ServeClient extends Thread {

    private Socket client;

    private ServeClient()
    {

    }

    public ServeClient(Socket client)
    {
        this.client = client;
    }

    public void run()
    {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
            String receivingData = (String) objectInputStream.readObject();

            // from ? to ? command ? data1 ? data2 ? ....

            String[] data = receivingData.split("\\?");

            String command = data[2];

            if(command.equals("createAccount"))
            {
                boolean isCreated = false;
                try {

                    User user = new User();
                    user.setName(data[3]);
                    user.setAccountNumber(data[4]);
                    user.setPassword(data[5]);
                    Account account = new Account();
                    isCreated = account.createAccount(user);

                }catch (Exception e)
                {

                }

                String delimiter = "?";

                String sendingData = "" + delimiter + "" + delimiter  + command + delimiter + isCreated;

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectOutputStream.writeObject(sendingData);

                objectInputStream.close();
                objectOutputStream.close();
                client.close();

            }

            else if(command.equals("login"))
            {
                boolean isLogin = false;

                try{

                    String accountNumber = data[3];
                    String password = data[4];

                    Account account = new Account();
                    isLogin = account.login(accountNumber, password);

                }catch(Exception e)
                {

                }
                System.out.println(isLogin);

                String delimiter = "?";

                String sendingData = "" + delimiter + "" + delimiter  + command + delimiter + isLogin;

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectOutputStream.writeObject(sendingData);

                objectInputStream.close();
                objectOutputStream.close();
                client.close();
            }

            else if(command.equals("initializeUserInfo"))
            {
                String user = data[0];

                File folder = new File("src/resources/");
                File[] files = folder.listFiles();

                for(File f: files)
                {
                    if(f.getName().equals(user))
                    {
                        FileInputStream fin  = new FileInputStream(f);
                        ObjectInputStream objectInputStream1 = new ObjectInputStream(fin);

                        User tmpUser = (User) objectInputStream1.readObject();

                        String delimiter = "?";
                        String sendingData = "" + delimiter + user + delimiter  + command + delimiter + tmpUser.getName() + delimiter + tmpUser.getAccountNumber() + delimiter + tmpUser.getBalance();

                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                        objectOutputStream.writeObject(sendingData);

                        objectInputStream.close();
                        fin.close();
                        objectInputStream1.close();
                        objectOutputStream.close();
                        client.close();

                        break;
                    }
                }

            }

            else if(command.equals("withdraw"))
            {
                String user = data[0];
                String ammount = data[3];

                Account account = new Account();
                boolean isWithdraw = account.withdraw(ammount, user);

                String delimiter = "?";
                String sendingData = "" + delimiter + user + delimiter  + command + delimiter + isWithdraw;

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectOutputStream.writeObject(sendingData);

                objectInputStream.close();
                objectOutputStream.close();
                client.close();

            }

            else if(command.equals("deposit"))
            {
                String user = data[0];
                String ammount = data[3];

                Account account = new Account();
                boolean isDeposit = account.deposit(ammount, user);

                String delimiter = "?";
                String sendingData = "" + delimiter + user + delimiter  + command + delimiter + isDeposit;

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectOutputStream.writeObject(sendingData);

                objectInputStream.close();
                objectOutputStream.close();
                client.close();

            }

            else if(command.equals("transferBalance"))
            {
                String from = data[0];
                String to = data[1];
                String ammount = data[3];

                Account account = new Account();
                boolean isTransfer = account.transferBalance(from, to, ammount);

                String delimiter = "?";
                String sendingData = "" + delimiter + from + delimiter  + command + delimiter + isTransfer;

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectOutputStream.writeObject(sendingData);

                objectInputStream.close();
                objectOutputStream.close();
                client.close();
            }





        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
