package GeneralWebsiteFiles;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVEditor {
    private File file;

    public CSVEditor(File file){
        this.file = file;
    }

    private CSVWriter createWriter(boolean append) throws IOException{
        FileWriter outputFile = new FileWriter(this.file, append);
        return new CSVWriter(outputFile);
    }

    private CSVReader openReader() throws IOException{
        FileReader inputFile = new FileReader(this.file);
        return new CSVReader(inputFile);
    }

    public void addRow(String[] arr) throws IOException{
        CSVWriter writer = createWriter(true);
        writer.writeNext(arr);

        writer.close();
    }

    public void addRow(String[] arr, int rowNum) throws IOException{
        List<String[]> allElements = null;
        CSVReader reader = openReader();
        try{
            allElements = reader.readAll();
        } catch(Exception e){
            e.getStackTrace();
        }

        allElements.add(rowNum, arr);

        CSVWriter writer = createWriter(true);
        writer.writeAll(allElements);

        writer.close();
        reader.close();
    }

    public void deleteRow() throws IOException{
        List<String[]> allElements = null;
        CSVReader reader = openReader();
        try{
            allElements = reader.readAll();
        } catch(Exception e){
            e.getStackTrace();
        }

        allElements.remove(allElements.size()-1);

        CSVWriter writer = createWriter(false);
        writer.writeAll(allElements);

        writer.close();
        reader.close();
    }

    public void deleteRow(int rowNum) throws IOException{
        List<String[]> allElements = null;
        CSVReader reader = openReader();
        try{
            allElements = reader.readAll();
        } catch(Exception e){
            e.getStackTrace();
        }

        allElements.remove(rowNum);

        CSVWriter writer = createWriter(false);
        writer.writeAll(allElements);

        writer.close();
        reader.close();
    }

    // General method to add any type of column to a CSV file.
    public void addColumn(String[] arr) throws IOException{
        List<String[]> allElements = null;
        CSVReader reader = openReader();
        try{
            allElements = reader.readAll();
        } catch(Exception e){
            e.getStackTrace();
        }

        for(int i = 0; i < allElements.size(); i++){
            if(i <= arr.length-1){
                allElements.set(i, mergeArrays(allElements.get(i), arr[i]));
            }
            else{
                allElements.set(i, mergeArrays(allElements.get(i), "0"));
            }
        }

        CSVWriter writer = createWriter(false);
        writer.writeAll(allElements);
        
        writer.close();
        reader.close();
    }

    // Takes in a string array of a website url and the price
    public void addWebColumn(String[] arr) throws IOException{
        List<String[]> allElements = null;
        CSVReader reader = openReader();
        try{
            allElements = reader.readAll();
        } catch(Exception e){
            e.getStackTrace();
        }

        if(allElements.isEmpty()){
            addRow(arr);
        }
        else{
            for(int i = 0; i < allElements.size(); i++){
                if(allElements.get(i)[0].equals(arr[0])){
                    allElements.set(i, mergeArrays(allElements.get(i), arr[1]));
                    CSVWriter writer = createWriter(false);
                    writer.writeAll(allElements);
                    writer.close();
                    break;
                }
                else if(i >= allElements.size() - 1){
                    addRow(arr);
                }
            }
        }
        reader.close();
    }

    public void sortByLength() throws IOException{
        List<String[]> allElements = null;
        CSVReader reader = openReader();
        try{
            allElements = reader.readAll();
        } catch(Exception e){
            e.getStackTrace();
        }

        int n = allElements.size();
        for (int i = 1; i < n; ++i) {
            int key = allElements.get(i).length;
            String[] relatedKey = allElements.get(i);
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && allElements.get(j).length < key) {
                allElements.set(j+1, allElements.get(j));
                j = j - 1;
            }
            allElements.set(j+1, relatedKey);
        }

        CSVWriter writer = createWriter(false);
        writer.writeAll(allElements);
        
        writer.close();
        reader.close();
    }

    private String[] mergeArrays(String[] arr1, String element){
        int arr1Length = arr1.length;
        String[] returnedArr = new String[arr1Length+1];

        for(int i = 0; i < returnedArr.length; i++){
            if(i == returnedArr.length-1){
                returnedArr[i] = element;
            }
            else{
                returnedArr[i] = arr1[i];
            }
        }
        return returnedArr;
    }
}