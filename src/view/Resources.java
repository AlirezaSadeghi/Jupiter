package view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Resources 
{
	public static Image ASSEMBLE ;
	public static Image BACKWARD ;
	public static Image FORWARD ;
	public static Image UNDO ;
	public static Image REDO ;
	public static Image RUN ;
	public static Image STEP_BACK ;
	public static Image STEP_FORWARD ;
	public static Image PAUSE ;
	public static Image TERMINATE ;
	public static Image SAVE ;
	public static Image COPY ;
	public static Image CUT ;
	public static Image OPEN ;
	public static Image NEW ;
	public static Image PASTE ;
	public static Image REMOVE ;
	public static Image SAVE_AS ;
	public static Image DELETE_ALL ;
	public static Image SELECT_ALL ;
	public static Image BOLD ;
	public static Image ITALIC;
	public static Image UNDERLINED ;
	public static Image FONT_FAMILY;
	public static Image FONT_SIZE ;
	public static Image TRADE_MARK ; 
	public static Image STATISTICS ;
	public static Image COUNT ;
	public static Image JUPITER ;

	static{
		try{
			JUPITER = ImageIO.read(new File("Resources/Jupiter1.jpg"));
			COUNT = ImageIO.read(new File("Resources/count.png"));
			STATISTICS = ImageIO.read(new File("Resources/statistics.png"));
			TRADE_MARK = ImageIO.read(new File("Resources/trade_mark.jpg"));
			FONT_FAMILY = ImageIO.read(new File("Resources/family.png"));
			FONT_SIZE = ImageIO.read(new File("Resources/size.png"));
			BOLD = ImageIO.read(new File("Resources/bold.png"));
			ITALIC = ImageIO.read(new File("Resources/italic.png"));
			UNDERLINED = ImageIO.read(new File("Resources/underlined.png"));
			SELECT_ALL = ImageIO.read(new File("Resources/select.png"));
			SAVE_AS = ImageIO.read(new File("Resources/save_as.png"));
			DELETE_ALL= ImageIO.read(new File("Resources/delete_all.png"));
			REMOVE = ImageIO.read(new File("Resources/remove.png"));
			PASTE = ImageIO.read(new File("Resources/paste.png")) ;
			NEW = ImageIO.read(new File("Resources/new.png")) ;
			ASSEMBLE = ImageIO.read(new File("Resources/Assemble.png"));
			BACKWARD = ImageIO.read(new File("Resources/backward.png"));
			FORWARD = ImageIO.read(new File("Resources/forward2.png"));
			UNDO = ImageIO.read(new File("Resources/undo.png"));
			REDO = ImageIO.read(new File("Resources/redo.png"));
			RUN = ImageIO.read(new File("Resources/play.png"));
			STEP_BACK = ImageIO.read(new File("Resources/playback.png"));
			PAUSE = ImageIO.read(new File("Resources/pause.png"));
			TERMINATE = ImageIO.read(new File("Resources/terminate.png"));
			SAVE = ImageIO.read(new File("Resources/save.png"));
			COPY = ImageIO.read(new File("Resources/copy.png"));
			CUT = ImageIO.read(new File("Resources/cut.png"));
			OPEN = ImageIO.read(new File("Resources/open.png"));
	}catch(IOException e ){e.printStackTrace();}
	}
}