package student;


import java.io.*;

public class StudentController {

    public boolean addStudent(Student student) throws IOException {

        if(student.getName().isEmpty() || student.getRoll().isEmpty() || student.getCls().isEmpty() || student.getRegistrationNumber().isEmpty() || student.getPassword().isEmpty())
            return false;

        File folder = new File("src/resources/");
        File[] listOfFiles = folder.listFiles();

        for(File f: listOfFiles)
        {
            if(f.getName().equals(student.getRegistrationNumber()))
                return false;
        }

        FileOutputStream fout = new FileOutputStream("src/resources/" + student.getRegistrationNumber());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fout);

        objectOutputStream.writeObject(student);

        fout.close();
        objectOutputStream.close();

        return true;
    }

    public boolean removeStudent(String registrationNumber) throws IOException {
        if(registrationNumber.isEmpty())
            return false;

        File file = new File("src/resources/" + registrationNumber);

        file.delete();

        if(file.exists())
            return false;
        else
            return true;
    }


}
