/***********************************/
/* NEuclideanDistance              */
/*---------------------------------*/
/* Ver.4                           */
/* Date:081228                     */
/*---------------------------------*/
/* Program by Fu,Yu-Hsiang in Aizu */
/***********************************/

//NEuclideanDistance
public class NEuclideanDistance{
    public int victorLength;

    //NEuclideanDistance
    public NEuclideanDistance(int x){
        //Initialization
        Initialization(x);
    }

    //Initialization
    private void Initialization(int x){
        victorLength=x;
    }

    //Calculate
    public double Calculate(int[] x,int[] y){
        double distance=0;

        //Distance formula
        for(int a=0;a<victorLength;a++){
            distance+=Math.pow((x[a]-y[a]),2);
        }

        //Check NaN
        if(Double.isNaN(distance)){
            distance=1;
        }

        //Normalization
        distance=(double)(1-Math.sqrt(distance/victorLength));

        return distance;
    }
}