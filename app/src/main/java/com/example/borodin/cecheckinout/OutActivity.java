package com.example.borodin.cecheckinout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.Space;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class OutActivity extends AppCompatActivity
{
	private static final String TAG = "OutActivity_TEST";
	private LinearLayout outLayout;
	private Project correntProject;
	private MessegeInOut correntMessege;
	private ArrayList<Question> correntQuestions;
	private SQLiteDatabase db;
	private ProjectSQLiteOpenHelper dbhelper;
	private ImageView tempImageView = null;
	private Button tempButton = null;
	private EditText managerEmail = null;
	private EditText managerName = null;
	private Switch switchmanagercopy = null;
	private SharedPreferences preferences;

	// Storage Permissions
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_out);

		initialase();
		makeList();
	}

	// checing if we need to send copy to manager
	private void checkformanageremail()
	{
		if(switchmanagercopy.isChecked())
		{
			Utilities.print(TAG, "switch is On!");
			if(managerEmail != null) managerEmail.setVisibility(View.VISIBLE);
		}
		else
		{
			Utilities.print(TAG, "switch is Off!");
			if(managerEmail != null) managerEmail.setVisibility(View.INVISIBLE);
		}
		Utilities.print(TAG, "coling savecheckemailweitch()");
		savecheckemailswitch();
	}

	// saving corent status of the email manager switch
	private void savecheckemailswitch()
	{
		if(switchmanagercopy != null )
		{
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean(getString(R.string.manageremailswitchsave), switchmanagercopy.isChecked());
			if (switchmanagercopy.isChecked())
			{
				Utilities.print(TAG, "Saving " + managerEmail.getText().toString() + " for next time");
				editor.putString(getString(R.string.TheManagerEmail), managerEmail.getText().toString());
			}
			editor.commit();
		}
	}

	private void initialase()
	{
		preferences = getSharedPreferences(getString(R.string.setings_for_update), this.MODE_PRIVATE);
		correntProject = (Project) getIntent().getParcelableExtra("project");
		correntMessege = (MessegeInOut) getIntent().getParcelableExtra("message");

		switchmanagercopy = (Switch) findViewById(R.id.switchmanagercopy);
		managerEmail = (EditText) findViewById(R.id.managerEmailCopy);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		if(switchmanagercopy != null && managerEmail != null)
		{
			Utilities.print(TAG, "Seting the defolt options for check In Out Activity ");
			switchmanagercopy.setChecked(preferences.getBoolean(getString(R.string.manageremailswitchsave), false));
			managerEmail.setText(preferences.getString(getString(R.string.TheManagerEmail), ""));

			switchmanagercopy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					checkformanageremail();
				}
			});
		}

		checkformanageremail();

		correntMessege.setProjesctName(correntProject.getName());
		dbhelper = new ProjectSQLiteOpenHelper(this);
		db = dbhelper.getReadableDatabase();
		if (correntProject != null)
			correntQuestions = dbhelper.readQuestionsByProjectId(db, correntProject.getId());
		else
			Toast.makeText(this, "Problem with project Sorry ", Toast.LENGTH_SHORT).show();

		TextView tv = (TextView) findViewById(R.id.out_title);
		tv.setText(correntMessege.getSiteStoreNumber());
		setTitle(correntProject.getName());
	}

	private void makeList()
	{
		outLayout = (LinearLayout) findViewById(R.id.out_list);
		if (correntQuestions != null)
		{
			for (int i = 0; i < correntQuestions.size(); i++)
			{
				if (correntQuestions.get(i).getQuestionType().toLowerCase().trim().equals("q"))
				{
					CheckBox cb = new CheckBox(this);
					cb.setText(correntQuestions.get(i).getQestion());
					cb.setChecked(false);
					outLayout.addView(cb);
				} else if (correntQuestions.get(i).getQuestionType().toLowerCase().trim().equals("c"))
				{
					LinearLayout ll = new LinearLayout(this);
					ll.setOrientation(LinearLayout.HORIZONTAL);

					TextView tv = new TextView(this);
					tv.setText(correntQuestions.get(i).getQestion());

					EditText et = new EditText(this);
					et.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
					ll.addView(tv);
					ll.addView(et);
					outLayout.addView(ll);
				} else if (correntQuestions.get(i).getQuestionType().toLowerCase().trim().equals("b"))
				{
					// making temp layout to pleas img to the right on button
					LinearLayout ll = new LinearLayout(this);
					ll.setOrientation(LinearLayout.HORIZONTAL);
					// ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
					Space sp = new Space(this);
					sp.setLayoutParams(params);

					final ImageView img = new ImageView(this);
					img.setLayoutParams(params);
					img.setImageResource(R.drawable.ic_hplogo);

					final String photoname_btn = correntQuestions.get(i).getQestion();
					Button btn = new Button(this);
					btn.setText(photoname_btn);
					btn.setEnabled(true);
					btn.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							// TODO: 1/8/2017 pass the button name here
							tempButton = (Button)v;
							addPhoto(img);
							if (v instanceof  Button) Utilities.print(TAG, "It is the BUTTON!!!");
						}

					});

					ll.addView(btn);
					ll.addView(sp);
					ll.addView(img);

					outLayout.addView(ll);
				}
			}
			TextView temptext = new TextView(this);
			managerName = new EditText(this);
			managerName.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
			LinearLayout templl = new LinearLayout(this);
			temptext.setText("Customer Contact: ");
			templl.addView(temptext);
			templl.addView(managerName);
			outLayout.addView(templl);

		} else
			Toast.makeText(this, "Problem with Questions Sorry ", Toast.LENGTH_SHORT).show();
	}

	private void addPhoto(ImageView img)
	{
		tempImageView = img;
		int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
		if (permission != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
		}
		if (permission == PackageManager.PERMISSION_GRANTED)
		{
			selectImage();
		} else
		{
			Utilities.print(TAG, "Permission not granted!!!");
		}
	}

	private void selectImage()
	{

		final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

		AlertDialog.Builder builder = new AlertDialog.Builder(OutActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(options, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int item)
			{
				if (options[item].equals("Take Photo"))
				{
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, 3);
				} else if (options[item].equals("Choose from Gallery"))
				{
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 4);

				} else if (options[item].equals("Cancel"))
				{
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Utilities.print(TAG, "Got some result !!! \t requst code : " + requestCode + " \t result Code : " + resultCode);
		Utilities.print(TAG, "\t\t = Result code : " + resultCode + " OK " + RESULT_OK + " \t " + "requst Code " + requestCode);
		if (resultCode == RESULT_OK)
		{
			switch (requestCode)
			{
				case 0:
					Bitmap bs = BitmapFactory.decodeByteArray(data.getByteArrayExtra("byteArray"), 0, data.getByteArrayExtra("byteArray").length);
					if (bs == null) Utilities.print(TAG, "did not gor BS");
					else Utilities.print(TAG, "GOT BS" + bs.toString());
					sendCheckOut(bs);
					break;
				case 3: // Take Photo
					File f = new File(Environment.getExternalStorageDirectory().toString());
					for (File temp : f.listFiles())
					{
						if (temp.getName().equals("temp.jpg"))
						{
							f = temp;
							break;
						}
					}
					try
					{
						Bitmap bitmap;
						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

						bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

						if (tempImageView != null)
						{
							Bitmap profileImage = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
							tempImageView.setImageBitmap(profileImage);
						}

						// Making path to the photo
						String path = android.os.Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "DCIM" + File.separator + "Camera";

						// deleting temp file
						f.delete();
						OutputStream outFile = null;
						String tempphotofilename = String.valueOf(System.currentTimeMillis()) + ".jpg";
						File file = new File(path, tempphotofilename);
						try
						{
							outFile = new FileOutputStream(file);
							bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
							outFile.flush();
							outFile.close();
						} catch (FileNotFoundException e)
						{
							e.printStackTrace();
						} catch (IOException e)
						{
							e.printStackTrace();
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						Utilities.print(TAG, " Taking Photo with name: " + file.getPath() );
						correntMessege.addFile(path+"/"+tempphotofilename, tempButton.getText().toString());
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					break;
				case 4: // Choose from Gallery
					Uri selectedImage = data.getData();
					String[] filePath = {MediaStore.Images.Media.DATA};
					Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
					c.moveToFirst();
					int columnIndex = c.getColumnIndex(filePath[0]);
					String picturePath = c.getString(columnIndex);
					c.close();
					Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
					Utilities.print(TAG, "path of image from gallery: " + picturePath + "");
					correntMessege.addFile(picturePath, tempButton.getText().toString());
					if (tempImageView != null)
					{
						Bitmap profileImage = Bitmap.createScaledBitmap(thumbnail, 220, 220, true);
						tempImageView.setImageBitmap(profileImage);
					}
					break;
			}
		}
		if (requestCode == 5)
		{
			// wait on user to send email befor start Main Activity ( go back to Main page )
			Utilities.print(TAG, "Finished sending Email need to go back to Main Activity");
			Intent intent = new Intent(OutActivity.this, MainActivity.class);
			startActivity(intent);
		/*****************************************************************************************/
			// Open pdf for testing
			// openmanfile(pdfUri.getPath());
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		savecheckemailswitch();
	}

	public void onClickOutSend(View v)
	{

		Utilities.print(TAG, "On Check in Clicked");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		CeckOutData ceckOutData = new CeckOutData(this);
		ceckOutData.readCeckOutData();
		ceckOutData.setCename(preferences.getString("thech_name", ""));
		ceckOutData.printCeckOutData(TAG);
		new UpdateServer(this, ceckOutData);

		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("ISIN", false);
		editor.apply();
		if ( askPermissionWrite() )
		{
			Intent intent = new Intent(this, SignatureActivity.class);
			intent.putExtra(getString(R.string.TheManagerName), managerName.getText().toString());
			startActivityForResult(intent, 0);
		}

	}

	private Uri storeimage(Bitmap image)
	{

		int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permission != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
		}
		if (permission == PackageManager.PERMISSION_GRANTED)
		{
			String pathsig = MediaStore.Images.Media.insertImage(getContentResolver(), image, "title", null);
			Uri fileuri = Uri.parse(pathsig);
			return fileuri;
		} else
		{
			Utilities.print(TAG, "Permission not granted!!!");
		}
		return null;
	}
	private boolean askPermissionWrite()
	{
		int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permission != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
		}
		if (permission == PackageManager.PERMISSION_GRANTED)
		{
			return true;
		} else
		{
			return false;
		}
	}

	private void sendCheckOut(Bitmap signature)
	{
		Uri screensig = storeimage(signature);

		correntMessege.setSignatur(Utilities.getRealPathFromURI(this, screensig));

		StringBuilder sendmassege = new StringBuilder();
		sendmassege.append("Project : " + correntProject.getName() + Utilities.newline);
		sendmassege.append("Site: " + correntMessege.getSiteStoreNumber() + Utilities.newline);

		int childcount = outLayout.getChildCount();
		for (int i = 0; i < childcount - 1; i++)
		{
			View view = outLayout.getChildAt(i);
			if (view instanceof CheckBox)
			{
				if (((CheckBox) view).isChecked())
				{
					sendmassege.append(((CheckBox) view).getText().toString() + " is complete" + Utilities.newline);
					String str = ((CheckBox) view).getText().toString() + " is complete" + Utilities.newline;
					correntMessege.addChecklist(str, MessegeInOut.ISCHEKET );
				} else
				{
					sendmassege.append(((CheckBox) view).getText().toString() + " is not complete" + Utilities.newline);
					String str = ((CheckBox) view).getText().toString() + " is not complete" + Utilities.newline;
					correntMessege.addChecklist(str, MessegeInOut.ISNOTCHEKET );
				}
			} else if (view instanceof LinearLayout)
			{
				int inChild = ((LinearLayout) view).getChildCount();
				for (int c = 0; c < inChild; c++)
				{
					View inview = ((LinearLayout) view).getChildAt(c);
					if (inview instanceof EditText)
					{
						sendmassege.append(" : " + ((EditText) inview).getText().toString() + Utilities.newline);
						String str = (" : " + ((EditText) inview).getText().toString() + Utilities.newline);
						correntMessege.addChecklist(str, MessegeInOut.ISTEXT );
					} else if (inview instanceof TextView)
					{
						if ( inview instanceof  Button)
						{
							// caching Photo buttons from text view.
							Utilities.print(TAG, "It is the Button for Photo: " + ((Button) inview).getText().toString() );
						}
						else
						{
							sendmassege.append(((TextView) inview).getText().toString());
							String str = ((TextView) inview).getText().toString();
							correntMessege.addChecklist(str, MessegeInOut.ISTEXTFILD );
						}
					}
				}
			}
		}
		// TODO: 8/29/2016  Need to move this thing to beter plse.
		sendmassege.append("\n" + Utilities.getTimeZon());
		String timenow = Utilities.getTime();
		sendmassege.append(": " + timenow);
		correntMessege.setTimeOut(timenow);
		Utilities.print(TAG, sendmassege.toString());
		correntMessege.setOutMasseg(sendmassege.toString());

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Attention");
		alert.setMessage(R.string.alertmassege);
		alert.setPositiveButton("OK",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Uri pdfUri =  makePDF(correntMessege);

						Intent intentout = new Intent(Intent.ACTION_SEND);
						intentout.setType("image/png");
						if (managerEmail != null)
							intentout.putExtra(Intent.EXTRA_EMAIL, new String[]{correntProject.getEmail(), managerEmail.getText().toString()});
						else
							intentout.putExtra(Intent.EXTRA_EMAIL, new String[]{correntProject.getEmail()});
						intentout.putExtra(Intent.EXTRA_SUBJECT, "Checking out Store " + correntMessege.getSiteStoreNumber());
						intentout.putExtra(Intent.EXTRA_TEXT, correntMessege.getOutMasseg());
						intentout.putExtra(Intent.EXTRA_STREAM, pdfUri);
						try
						{
							startActivityForResult(intentout, 5);
							Utilities.print(TAG, "Email was sended sucsesefuly !");
						} catch (ActivityNotFoundException e)
						{
							Toast.makeText(OutActivity.this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
							Utilities.print(TAG, "There are no email clients installed.");
						}
					}
				});
		alert.show();
	}

	public Uri makePDF(MessegeInOut strmeseg)
	{
		CheckoutPDF mypdf = new CheckoutPDF(this, correntProject);
		mypdf.setMassege(strmeseg);
		mypdf.makePDF();
		return Uri.fromFile(new File(mypdf.getFilepath()));

	}

	private void openmanfile(String filepath)
	{
		File file = new File(filepath);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
	}

	public void onClickOutCancel(View v)
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
