package zzleep.etl;

public class EtlBoot
{
    public static void main( String[] args )
    {
        Etl etl = new Etl();
        Thread t = new Thread(etl);
        t.start();
    }
}
