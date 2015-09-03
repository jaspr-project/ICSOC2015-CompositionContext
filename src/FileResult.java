import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileResult {

		private FileWriter fileWriter;
		private BufferedWriter bufferedWriter;
		
		public FileResult(String fileName)
		{
			try {
				fileWriter= new FileWriter(fileName);
				bufferedWriter=new BufferedWriter(fileWriter);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void writeln(String text)
		{
			try
			{
				bufferedWriter.write(text+"\n");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		  }
		
		public void write(String text)
		{
			try
			{
				bufferedWriter.write(text);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		  }
		
		public void close()
		{
			try {
				bufferedWriter.close();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
