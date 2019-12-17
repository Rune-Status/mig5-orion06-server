import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class a {

	public static final String backup_directory = "./data/backups/";
	
	public static void main(String[] args){
		moveLogs();
	}
	
	public static void moveLogs(){
		File folder = new File(backup_directory);
		File[] listOfBackupFolders = folder.listFiles();
		for (File backUpFolder : listOfBackupFolders) {
			File[] listOfBackupFolders2 = backUpFolder.listFiles();
			for (File backUpFolderFiles : listOfBackupFolders2) {
				if(backUpFolderFiles.getName().toLowerCase().equals("logs")){
					File[] listOfLogs = backUpFolderFiles.listFiles();
					for (File log : listOfLogs) {
						File path = new File("./data/move/"+backUpFolder.getName());
						File f = new File("./data/move/"+backUpFolder.getName()+"/"+log.getName());
						try {
							path.mkdirs();
							copyFileUsingJava7Files(log, f);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		    }
		}
	}
	
	private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
	    Files.copy(source.toPath(), dest.toPath());
	}
	
}
