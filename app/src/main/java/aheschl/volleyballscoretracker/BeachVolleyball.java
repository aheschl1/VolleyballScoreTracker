package aheschl.volleyballscoretracker;

import android.os.Build;
import android.os.VibrationEffect;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

class BeachVolleyball extends VolleyballMatch {


    @Override
    void homeScoreUp(MainActivity mainActivity) {

        homeScore++;

        if(mainActivity.mySharedPreferences.getAnimations() == Constants.ON) {

            mainActivity.mySharedPreferences.setHomeScore(homeScore);

            mainActivity.homeButton.startAnimation(mainActivity.slideUpHome);

            mainActivity.homeButton2.setText(String.valueOf(mainActivity.mySharedPreferences.getHomeScore()));

        }else{

            mainActivity.mySharedPreferences.setHomeScore(homeScore);

            mainActivity.homeButton.setText(String.valueOf(mainActivity.mySharedPreferences.getHomeScore()));

        }

        mainActivity.undoHistory.add(Constants.HOME_SCORE);

        mainActivity.mySharedPreferences.setServer(Constants.HOME);
        mainActivity.setServer();


        if(mainActivity.mySharedPreferences.getVibrate() == Constants.ON) {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mainActivity.vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                mainActivity.vibrator.vibrate(50);
            }
        }

        if(mainActivity.mySharedPreferences.getNotifyAtEnd() == Constants.ON) {

            if (mainActivity.isTieBreaker() && mainActivity.mySharedPreferences.get15PointTieBreaker()) {

                if (homeScore > 14 && homeScore - 1 > awayScore) {

                    if (mainActivity.mySharedPreferences.gettNumberOfSets() == 3) {
                        if (mainActivity.homeSetNumber.getText().toString().equals("1")) {

                            mainActivity.homeSetNumber.setText("2");
                            mainActivity.mySharedPreferences.setHomeSetsWon(2);

                            mainActivity.displayWin("home");

                        }
                    } else {
                        if (mainActivity.homeSetNumber.getText().toString().equals("2")) {

                            mainActivity.homeSetNumber.setText("3");
                            mainActivity.mySharedPreferences.setHomeSetsWon(3);

                            mainActivity.displayWin("home");

                        }
                    }
                } else if ((homeScore + awayScore) % 5 == 0) {

                    if (mainActivity.mySharedPreferences.getShowMessageForSideChange() == Constants.ON) {

                        new AlertDialog.Builder(mainActivity)
                                .setTitle("Switch sides")
                                .setMessage("Time to change sides. " +
                                        "Click SWITCH SIDES to change sides. - You can turn off this prompt in settings.")
                                .setPositiveButton("SWITCH SIDES", (dialogInterface, i) -> mainActivity.changeSides()).setNegativeButton("DON'T SWITCH SIDES", null).setCancelable(false).show();
                    } else {

                        mainActivity.changeSides();

                        Toast.makeText(mainActivity, "Sides changed", Toast.LENGTH_SHORT).show();

                    }

                }

            } else {

                if (homeScore > 20 && homeScore - 1 > awayScore) {

                    if (mainActivity.mySharedPreferences.gettNumberOfSets() == 3) {
                        if (mainActivity.homeSetNumber.getText().toString().equals("1")) {

                            mainActivity.homeSetNumber.setText("2");
                            mainActivity.mySharedPreferences.setHomeSetsWon(2);

                            mainActivity.displayWin("home");

                            return;
                        }
                    } else {
                        if (mainActivity.homeSetNumber.getText().toString().equals("2")) {

                            mainActivity.homeSetNumber.setText("3");
                            mainActivity.mySharedPreferences.setHomeSetsWon(3);

                            mainActivity.displayWin("home");

                            return;
                        }
                    }

                    new AlertDialog.Builder(mainActivity)
                            .setTitle(mainActivity.mySharedPreferences.getHomeTeamName() + " won the set")
                            .setMessage("Click PREPARE NEW SET to change courts, clear scores, clear timeouts, and add a set to "
                                    + mainActivity.mySharedPreferences.getHomeTeamName() + ". You can turn off this dialog in settings.")
                            .setPositiveButton("PREPARE NEW SET", (dialog, which) -> mainActivity.prepareNewSet())
                            .setNegativeButton("Cancel", null).setCancelable(false)
                            .show();

                }else if((homeScore + awayScore)% 7 == 0){
                    if (mainActivity.mySharedPreferences.getShowMessageForSideChange() == Constants.ON) {

                        new AlertDialog.Builder(mainActivity)
                                .setTitle("Switch sides")
                                .setMessage("Time to change sides. " +
                                        "Click SWITCH SIDES to change sides. - You can turn off this prompt in settings.")
                                .setPositiveButton("SWITCH SIDES", (dialogInterface, i) -> mainActivity.changeSides()).setNegativeButton("DON'T SWITCH SIDES", null).setCancelable(false).show();
                    } else {

                        mainActivity.changeSides();

                        Toast.makeText(mainActivity, "Sides changed", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }
    }
    @Override
    void awayScoreUp(MainActivity mainActivity) {

        awayScore++;

        if(mainActivity.mySharedPreferences.getAnimations() == Constants.ON) {

            mainActivity.mySharedPreferences.setAwayScore(awayScore);

            mainActivity.awayButton.startAnimation(mainActivity.slideUpAway);

            mainActivity.awayScore2.setText(String.valueOf(mainActivity.mySharedPreferences.getAwayScore()));

        }else{

            mainActivity.mySharedPreferences.setAwayScore(awayScore);

            mainActivity.awayButton.setText(String.valueOf(mainActivity.mySharedPreferences.getAwayScore()));

        }

        mainActivity.undoHistory.add(Constants.AWAY_SCORE);


        mainActivity.mySharedPreferences.setServer(Constants.AWAY);
        mainActivity.setServer();

        if(mainActivity.mySharedPreferences.getVibrate() == Constants.ON) {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mainActivity.vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                mainActivity.vibrator.vibrate(50);
            }
        }

        if(mainActivity.mySharedPreferences.getNotifyAtEnd() == Constants.ON) {

            if (mainActivity.isTieBreaker() && mainActivity.mySharedPreferences.get15PointTieBreaker()) {

                if (awayScore > 14 && awayScore - 1 > homeScore) {

                    if (mainActivity.mySharedPreferences.gettNumberOfSets() == 3) {
                        if (mainActivity.awaySetNumber.getText().toString().equals("1")) {

                            mainActivity.awaySetNumber.setText("2");
                            mainActivity.mySharedPreferences.setAwaySetsWon(2);

                            mainActivity.displayWin("away");

                        }
                    } else {
                        if (mainActivity.awaySetNumber.getText().toString().equals("2")) {

                            mainActivity.awaySetNumber.setText("3");
                            mainActivity.mySharedPreferences.setAwaySetsWon(3);

                            mainActivity.displayWin("away");

                        }
                    }
                }else if ((homeScore + awayScore) % 5 == 0) {

                    if (mainActivity.mySharedPreferences.getShowMessageForSideChange() == Constants.ON) {

                        new AlertDialog.Builder(mainActivity)
                                .setTitle("Switch sides")
                                .setMessage("Time to change sides. " +
                                        "Click SWITCH SIDES to change sides. - You can turn off this prompt in settings.")
                                .setPositiveButton("SWITCH SIDES", (dialogInterface, i) -> mainActivity.changeSides()).setNegativeButton("DON'T SWITCH SIDES", null).setCancelable(false).show();
                    } else {

                        mainActivity.changeSides();

                        Toast.makeText(mainActivity, "Sides changed", Toast.LENGTH_SHORT).show();

                    }

                }

            } else {

                if (awayScore > 20 && awayScore - 1 > homeScore) {

                    if (mainActivity.mySharedPreferences.gettNumberOfSets() == 3) {
                        if (mainActivity.awaySetNumber.getText().toString().equals("1")) {

                            mainActivity.awaySetNumber.setText("2");
                            mainActivity.mySharedPreferences.setAwaySetsWon(2);

                            mainActivity.displayWin("away");

                            return;
                        }
                    } else {
                        if (mainActivity.awaySetNumber.getText().toString().equals("2")) {

                            mainActivity.awaySetNumber.setText("3");
                            mainActivity.mySharedPreferences.setAwaySetsWon(3);

                            mainActivity.displayWin("away");

                            return;
                        }
                    }

                    new AlertDialog.Builder(mainActivity)
                            .setTitle(mainActivity.mySharedPreferences.getAwayTeamName() + " won the set")
                            .setMessage("Click PREPARE NEW SET to change courts, clear scores, clear timeouts, and add a set to "
                                    + mainActivity.mySharedPreferences.getAwayTeamName() + ". You can turn off this dialog in settings.")
                            .setPositiveButton("PREPARE NEW SET", (dialog, which) -> mainActivity.prepareNewSet())
                            .setNegativeButton("Cancel", null).setCancelable(false)
                            .show();

                }else if ((homeScore + awayScore)%7 == 0) {

                    if (mainActivity.mySharedPreferences.getShowMessageForSideChange() == Constants.ON) {

                        new AlertDialog.Builder(mainActivity)
                                .setTitle("Switch sides")
                                .setMessage("Time to change sides. " +
                                        "Click SWITCH SIDES to change sides. - You can turn off this prompt in settings.")
                                .setPositiveButton("SWITCH SIDES", (dialogInterface, i) -> mainActivity.changeSides()).setNegativeButton("DON'T SWITCH SIDES", null).setCancelable(false).show();
                    } else {

                        mainActivity.changeSides();

                        Toast.makeText(mainActivity, "Sides changed", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        }

    }
    @Override
    void awayScoreDown(MainActivity mainActivity) {

        awayScore --;

        if(awayScore > -1) {

            if(mainActivity.mySharedPreferences.getAnimations() == Constants.ON) {

                mainActivity.awayButton.startAnimation(mainActivity.slideDownAway);

                mainActivity.mySharedPreferences.setAwayScore(awayScore);

                mainActivity.awayScore2.setText(String.valueOf(mainActivity.mySharedPreferences.getAwayScore()));

            }else{

                mainActivity.mySharedPreferences.setAwayScore(awayScore);

                mainActivity.awayButton.setText(String.valueOf(mainActivity.mySharedPreferences.getAwayScore()));

            }

        }else{
            awayScore++;
        }

        if(mainActivity.mySharedPreferences.getVibrate() == Constants.ON) {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mainActivity.vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                mainActivity.vibrator.vibrate(50);
            }
        }
    }
    @Override
    void homeScoreDown(MainActivity mainActivity) {

        homeScore --;

        if(homeScore > -1) {

            if(mainActivity.mySharedPreferences.getAnimations() == Constants.ON) {

                mainActivity.homeButton.startAnimation(mainActivity.slideDownHome);

                mainActivity.mySharedPreferences.setHomeScore(homeScore);

                mainActivity.homeButton2.setText(String.valueOf(mainActivity.mySharedPreferences.getHomeScore()));

            }else{

                mainActivity.mySharedPreferences.setHomeScore(homeScore);

                mainActivity.homeButton.setText(String.valueOf(mainActivity.mySharedPreferences.getHomeScore()));

            }

        }else{

            homeScore++;

        }

        if(mainActivity.mySharedPreferences.getVibrate() == Constants.ON) {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mainActivity.vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                mainActivity.vibrator.vibrate(50);
            }
        }

    }
    @Override
    int getHomeScore() {
        return homeScore;
    }
    @Override
    int getAwayScore() {
        return awayScore;
    }
    @Override
    void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }
    @Override
    void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

}
