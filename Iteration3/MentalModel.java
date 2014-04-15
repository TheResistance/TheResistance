
/**
 * Write a description of class MentalModel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MentalModel
{
    int neuroticism;
    double X,Y,thresh;
    MentalModel(int neuroticism, double thresh) {
        this.neuroticism = neuroticism;
        this.thresh = thresh; 
        this.X = X; 
    }
    void setX(double X) {
        this.X = X;
    }
    double getX() {
        return X;
    }
    void setY(double Y) {
        this.Y = Y;
    }
    double getY() {
        return Y;
    }
    double accuses() {
       if (X > thresh) {
           if (neuroticism<5) {
               X -= 0.1;
            } else {
                X += 0.1; 
            }
        } 
        if (X<thresh) {Y=Y-0.1;}
        return X; 
    }
    double denials() {
        return Y;
    }
    double suggestions() {
        return (X < thresh) ? Y + 0.05*neuroticism : Y; 
    }
        
}
