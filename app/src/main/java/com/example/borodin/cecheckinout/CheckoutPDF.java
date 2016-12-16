package com.example.borodin.cecheckinout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by borodin on 9/23/2016.
 */

public class CheckoutPDF
{
	// Storage Permissions
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE =   {
														Manifest.permission.READ_EXTERNAL_STORAGE,
														Manifest.permission.WRITE_EXTERNAL_STORAGE
													};
	private static final String TAG = "CheckoutPDF_TEST";
	private Context context = null;
	private Project project = null;
	private String filepath = null;
	private MessegeInOut massege = null;


	public CheckoutPDF(Context context, Project p)
	{
		this.context = context;
		this.project = p;
	}

	public void setProject(Project project)
	{
		this.project = project;
	}

	public String getFilepath()
	{
		return filepath;
	}

	public void setMassege(MessegeInOut massege)
	{
		this.massege = massege;
	}

	public void testpdf2()
	{
		Document document = new Document();
		filepath = Environment.getExternalStorageDirectory().getPath() + "/chektemp.pdf";
		deletefiel(filepath);
		try
		{
			int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
			if (permission != PackageManager.PERMISSION_GRANTED)
			{
				ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
			}

			PdfWriter.getInstance(document, new FileOutputStream(filepath));
			document.open();
			float fntSize, lineSpacing;
			fntSize = 16.7f;
			lineSpacing = 10f;
			Paragraph title = new Paragraph(new Phrase(lineSpacing, "Proof of Installation", FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, fntSize)));
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);


			PdfPTable tablehader = new PdfPTable(2);
			tablehader.setSpacingBefore(20);
			tablehader.setSpacingAfter(40);
			PdfPCell thcell1, thcell2;

			thcell1 = new PdfPCell(new Phrase("Site / Store Number: " + massege.getSiteStoreNumber()));
			thcell1.setBorder(Rectangle.NO_BORDER);
			tablehader.addCell(thcell1);

			thcell2 = new PdfPCell(new Phrase(massege.getProjesctName()));
			thcell2.setBorder(Rectangle.NO_BORDER);
			thcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablehader.addCell(thcell2);

			thcell1 = new PdfPCell(new Phrase("Email : " + project.getEmail()));
			thcell1.setBorder(Rectangle.NO_BORDER);
			tablehader.addCell(thcell1);

			thcell2 = new PdfPCell(new Phrase("Time zone " + Utilities.getTimeZon() ));
			thcell2.setBorder(Rectangle.NO_BORDER);
			thcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablehader.addCell(thcell2);

			thcell1 = new PdfPCell(new Phrase("Phone: " + project.getPhone()));
			thcell1.setBorder(Rectangle.NO_BORDER);
			tablehader.addCell(thcell1);

			thcell2 = new PdfPCell(new Phrase(massege.getTimeOut()));
			thcell2.setBorder(Rectangle.NO_BORDER);
			thcell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablehader.addCell(thcell2);
			document.add(tablehader);


			if (massege != null)
			{
				Utilities.print(TAG, "Signarture : " + massege.getSignatur());

				if (massege.getChecklist().size() > 0)
				{
					ArrayList<String> linmeinsel = new ArrayList<>();
					String correntline = "";
					PdfPTable table = new PdfPTable(2);
					table.setSpacingBefore(15);
					table.setSpacingAfter(51);
					table.setWidths(new int[]{1, 5});
					// Add ListItem checks
					PdfPCell cell1, cell2;
					for (int i = 0; i < massege.getChecklist().size(); i++)
					{
						final String mm = massege.getChecklist().get(i);
						final Integer ii = massege.getIschecked().get(i);
						if (mm != null && ii != null)
						{
							Utilities.print(TAG, "ii = " + ii + " mm = " + mm);
							if (ii == MessegeInOut.ISCHEKET)
							{
								cell1 = new PdfPCell(Image.getInstance(getImgFromDrawable(R.drawable.ic_action_check_true, 20, 20)));
								cell2 = new PdfPCell(new Phrase(mm));
								cell1.setBorder(Rectangle.BOTTOM);
								cell2.setBorder(Rectangle.BOTTOM);
								table.addCell(cell1);
								table.addCell(cell2);
							} else if (ii == MessegeInOut.ISNOTCHEKET)
							{
								cell1 = new PdfPCell(Image.getInstance(getImgFromDrawable(R.drawable.ic_action_check_false, 20, 20)));
								cell2 = new PdfPCell(new Phrase(mm));
								cell1.setBorder(Rectangle.BOTTOM);
								cell2.setBorder(Rectangle.BOTTOM);
								table.addCell(cell1);
								table.addCell(cell2);
							} else if (ii == MessegeInOut.ISTEXTFILD)
							{
								correntline = mm;
							} else if (ii == MessegeInOut.ISTEXT)
							{
								correntline += mm;
								linmeinsel.add(correntline);
							}
						}
					}
					// add first table
					document.add(table);

					table = new PdfPTable(1);
					table.setSpacingBefore(15);
					table.setSpacingAfter(51);
					table.setWidthPercentage(100);

					for (String lstr : linmeinsel)
					{
						cell1 = new PdfPCell(new Phrase(lstr));
						cell1.setBorder(Rectangle.BOTTOM);
						table.addCell(cell1);
						Utilities.print(TAG, "hte box : " + lstr);
					}
					// add second table
					document.add(table);
				}
			}

			Image img = resizeBitmap(massege.getSignatur(), 100, 100, true);
			document.add(img);

			ArrayList<String> photofiles = massege.getFilelist();
			if ( photofiles != null && photofiles.size() > 0 )
			{
				Utilities.print(TAG, "The number of files is: " + photofiles.size());
				if (photofiles.isEmpty()) Utilities.print(TAG, "photofile is empty !");
				else  Utilities.print(TAG, "photfile is not empty!");
				for (int i=0; i< photofiles.size(); i++)
				{
					Utilities.print(TAG, "entere for file # " + i + photofiles.get(i));
				}
				// here is the formate and size of the photos that going int to email
				if (!photofiles.isEmpty())
				{
					Utilities.print(TAG, "Additing a photo");
					for (String photo: photofiles)
					{
						Utilities.print(TAG, "Additing photo: " + photo);
						Image tempphotoinmg = resizeBitmap(photo, 200, 200, false);
						document.add(tempphotoinmg);
					}
				}
			}
		} catch (DocumentException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			document.close();
			clear();
		}
	}

	private byte[] getImgFromDrawable(int drID, int w, int h)
	{
		Drawable d = context.getResources().getDrawable(drID);
		Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
		Bitmap resized = Bitmap.createScaledBitmap(bitmap, w, h, true);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		resized.compress(Bitmap.CompressFormat.PNG, 50, stream);
		return stream.toByteArray();
	}

	public Image resizeBitmap(String photoPath, int targetW, int targetH, boolean rotate) throws IOException, BadElementException
	{
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(photoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0))
		{
			scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		}

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true; //Deprecated API 21

		Bitmap bitmapret = BitmapFactory.decodeFile(photoPath, bmOptions);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		if (rotate)
		{
			android.graphics.Matrix matrix = new android.graphics.Matrix();
			matrix.postRotate(-90);
			Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapret, 0, 0, bitmapret.getWidth(), bitmapret.getHeight(), matrix, true);
			rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		} else bitmapret.compress(Bitmap.CompressFormat.JPEG, 100, stream);

		Image retval = Image.getInstance(stream.toByteArray());
		stream.close();
		return retval;
	}

	private void clear()
	{
		deletefiel(massege.getSignatur());
		massege.clearCheckList();
		massege.setFilelist(null);
	}

	private void deletefiel(String path)
	{
		Utilities.print(TAG, "Deleting file : " + path);
		File file = new File(path);
		boolean filedeleted = file.delete();
		if (!filedeleted) Utilities.print(TAG, "File wos not deleted ! :(");
		else  Utilities.print(TAG, "File was deleted ! :)");
		if (file.exists())
		{
			Utilities.print(TAG, "File still here :( " + file.getPath() );
			try 				  { file.getCanonicalFile().delete(); }
			catch (IOException e) { e.printStackTrace(); }
			if (file.exists()) 	  { context.getApplicationContext().deleteFile(file.getName()); }
		}
		if (file.exists())
			Utilities.print(TAG, "File still here :( " + file.getPath() );
		else
			Utilities.print(TAG, "File Deleted :) " + file.getPath() );

		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
	}
}
