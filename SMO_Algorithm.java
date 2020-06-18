package sample;

import java.util.Random;

public class SMO_Algorithm {

    public static double[] SVM_test(String testdata,double[] alpha){
        int m=1372*20/100;
        String[] veri=testdata.split("\n");
        double[] predicted_labels=new double[m];
        for(int i=0;i<m;i++){
            String[] degerler=veri[i].split(",");
            if(Double.parseDouble(degerler[0])>alpha[i])
                predicted_labels[i]=1;
            else
                predicted_labels[i]=-1;
        }
        return predicted_labels;
    }

    public static double[] SVM_train(String trainingdata,double C,double tol,int max_passes){
        double b=0;
        int m=1372*80/100;
        int passes=0;
        int num_changed_alphas;
        double[] x=new double[m],y=new double[m];
        double[] alpha=new double[m];
        double[] E=new double[m];
        double L,H;
        double n;
        double b1,b2;
        String[] veri=trainingdata.split("\n");
        for (int a=0;a<m;a++) {
            alpha[a] = 0;
        }
        while (passes<m){
            num_changed_alphas=0;
            double fonksiyon=0;
            for(int i=0;i<m;i++){
                String[] degerler=veri[i].split(",");
                x[i]=Double.parseDouble(degerler[0]);
                y[i]=Double.parseDouble(degerler[1]);
                C=Double.parseDouble(degerler[2]);
                tol=Double.parseDouble(degerler[3]);
                max_passes=Integer.parseInt(degerler[4]);
                for (int k=0;k<m;k++){
                    fonksiyon+=((alpha[k]*y[k]*x[k]))+(((alpha[k]*y[k]*x[i])))+b;  //1
                }
                E[i]=fonksiyon-y[i];
                if(((y[i]*E[i]<-tol)&&(alpha[i]<C)) || ((y[i]*E[i]>tol)&&(alpha[i]>0))){
                    Random rnd=new Random();
                    int j=rnd.nextInt(m);
                    while (j==i){
                        j=rnd.nextInt(m);
                    }
                    fonksiyon=0;
                    for (int l=0;l<m;l++){
                        fonksiyon+=((alpha[l]*y[l]*x[l]))+(((alpha[l]*y[l]*x[j])))+b;  //1
                    }
                    E[j]=fonksiyon-y[j];
                    double old_a_i,old_a_j;
                    old_a_i=alpha[i];
                    old_a_j=alpha[j];
                    if (y[i]!= y[j]){          //2
                        L=Math.max(0,(alpha[j]-alpha[i]));
                        H=Math.min(C,(C+alpha[j]-alpha[i]));
                    }
                    else{                                       //3
                        L=Math.max(0,(alpha[i]+alpha[j]-C));
                        H=Math.min(C,(alpha[i]+alpha[j]));
                    }
                    if(L==H)
                        continue;
                    n=(2*x[i]+2*x[j])-2; //2 tane birim vektörden dolayı -2 yaptım  //4
                    if(n>=0)
                        continue;
                    alpha[j]-=(y[j]*(E[i]-E[j])/n);  //5
                    if(alpha[j]>H)                              //6
                        alpha[j]=H;
                    else if (L<=alpha[j] && alpha[j]<=H)
                        alpha[j]=alpha[j];
                    else if(alpha[j]<L)
                        alpha[j]=L;

                    if(alpha[j]<old_a_j){                       //mutlak deger islemi
                        if (old_a_j-alpha[j]<Math.pow(10,-5))
                            continue;
                    }
                    else{
                        if (alpha[j]-old_a_j<Math.pow(10,-5))
                            continue;
                    }

                    alpha[i]+=(y[i])*y[j]*(old_a_j-alpha[j]);    //7
                    b1=b-E[i]-(y[i]*(alpha[i]-old_a_i)*x[i])+(y[i]*(alpha[i]-old_a_i)*x[i])-(y[j]*(alpha[j]-old_a_j)*x[i])+(y[j]*(alpha[j]-old_a_j)*x[j]);   //8
                    b2=b-E[j]-(y[i]*(alpha[i]-old_a_i)*x[i])+(y[i]*(alpha[i]-old_a_i)*x[j])-(y[j]*(alpha[j]-old_a_j)*x[j])+(y[j]*(alpha[j]-old_a_j)*x[j]);   //9

                    if (0<alpha[i] && alpha[i]<C)           //10
                        b=b1;
                    else if (0<alpha[j] && alpha[j]<C)
                        b=b2;
                    else
                        b=(b1+b2)/2;

                    num_changed_alphas++;
                }
            }
            if (num_changed_alphas==0)
                passes++;
            else
                passes=0;
        }
        return alpha;
    }
}
