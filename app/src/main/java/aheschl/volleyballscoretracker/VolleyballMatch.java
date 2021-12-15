package aheschl.volleyballscoretracker;

abstract class VolleyballMatch {

    int homeScore = 0;
    int awayScore = 0;

    void homeScoreUp(MainActivity mainActivity){

    }
    void awayScoreUp(MainActivity mainActivity){

    }
    void awayScoreDown(MainActivity mainActivity){

    }
    void homeScoreDown(MainActivity mainActivity){

    }

    void setHomeScore(int homeScore){

    }

    void setAwayScore(int awayScore){

    }

    abstract int getHomeScore();
    abstract int getAwayScore();
}
