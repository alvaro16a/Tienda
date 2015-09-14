package tienda;
import java.sql.*;
import java.util.Scanner;

public class Tienda {
      
    public static void main(String[] args) {
        int N=0,aux,unidades,contador=100,Uactuales,id,i;
        int Precio=0,Nprecio;
        Scanner Lector = new Scanner(System.in);
        String Nombre = null;
        String user="root";
        String user2="alv123";
        String pasword="ak49zte";
        String pasword2="ak49zte";
        String url2="jdbc:mysql://db4free.net/productos16";
        String url="jdbc:mysql://localhost/productos";
        try{
        System.out.println("conectando abase de datos");
        Class.forName("com.mysql.jdbc.Driver");
        Connection con= DriverManager.getConnection(url2,user2,pasword);
        System.out.println("coneccion EXITOSA ...");
        Statement estado = con.createStatement();
        ResultSet resultado;
        resultado = estado.executeQuery("SELECT * FROM `productos`");
        while(resultado.next()){
            contador=resultado.getInt("ID");
        }
        for(i=1;i<=contador;i++){
             estado.executeUpdate("UPDATE `productos16`.`productos` SET `Iactual` = `Iinicial` WHERE `productos`.`ID` ="+i+"");
                        }
        while(N==0){
            System.out.println("1 AGREGAR PRODUCTO");
            System.out.println("2 BUSCAR PRODUCTO");
            System.out.println("3 ELIMINAR PRODUCTO");
            System.out.println("4 MOSTRAR INVENTARIO");
            System.out.println("5 REALIZAR VENTA");
            System.out.println("6 MOSTRAR GANANCIAS TOALES");
            System.out.println("7 SALIR");
            aux=Lector.nextInt();
            Lector.nextLine();
            switch(aux){
                case 1:
                    System.out.println("Digite el nombre del producto: ");
                    Nombre = Lector.nextLine();
                    System.out.println("Ingrese el precio del producto");
                    Precio = Lector.nextInt();
                    System.out.println("Ingrese el numero de unidades del procucto a ingresar");
                    unidades = Lector.nextInt();
                    estado.executeUpdate("INSERT INTO `productos16`.`productos` (`ID`, `Nombre`, `Iinicial`, `Iactual`, `Precio`) VALUES ('"+(++contador)+"', '"+Nombre+"', '"+unidades+"', '"+unidades+"', '"+Precio+"')");
                break;
                 case 2:
                    System.out.print("Ingrese el nombre del producto a buscar: ");
                    Nombre=Lector.nextLine(); 
                    resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `Nombre` LIKE '"+Nombre+"'");
                    if(resultado.next()==false){
                        System.out.println("el producto buscado no existe");
                    }
                    else{
                        System.out.println("ID"+"\t"+"Nombre"+"\t"+"Cantidad disponible"+"\t"+"Precio unidad");
                        resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `Nombre` LIKE '"+Nombre+"'");
                        while(resultado.next()){
                            System.out.println(resultado.getString("id")+"\t"+resultado.getString("Nombre")+"\t"+"\t"+resultado.getString("Iactual")+"\t"+"\t"+resultado.getString("Precio"));                   
                        }
                    }
                 break;    
                case 3:
                    System.out.println("Ingrese el nombre del producto a eliminar");
                    Nombre=Lector.nextLine(); 
                    resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `Nombre` LIKE '"+Nombre+"'");
                    if(resultado.next()==false){
                        System.out.println("el producto buscado no existe");
                    }
                    else{
                        resultado = estado.executeQuery("SELECT id FROM `productos` WHERE nombre='"+Nombre+"'");
                        resultado.next();
                        id = resultado.getInt("ID");
                        estado.executeUpdate("DELETE FROM `productos` WHERE `Nombre` LIKE '"+Nombre+"'");
                        for(i=(id+1);i<=contador;i++){
                             estado.executeUpdate("UPDATE `productos16`.`productos` SET `ID` = '"+(i-1)+"' WHERE `productos`.`ID` ="+i+"");
                        }
                        contador--;
                    }                   
                break;    
                case 4: 
                    resultado = estado.executeQuery("SELECT * FROM `productos`");
                    System.out.println("ID"+"\t"+"Nombre"+"\t"+"Cantidad disponible"+"\t"+"Precio unidad");
                    while(resultado.next()){
                         System.out.println(resultado.getString("id")+"\t"+resultado.getString("Nombre")+"\t"+"\t"+resultado.getString("Iactual")+"\t"+"\t"+resultado.getString("Precio"));                   
                    }
                break;
                case 5: 
                    System.out.println("Ingrese el nombre del producto a vender");
                    Nombre=Lector.nextLine(); 
                    resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `Nombre` LIKE '"+Nombre+"'");
                    if(resultado.next()==false){
                        System.out.println("el producto buscado no existe");
                    }
                    else{
                        resultado = estado.executeQuery("SELECT id FROM `productos` WHERE nombre='"+Nombre+"'");
                        resultado.next();
                        id = resultado.getInt("ID");
                        resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `Nombre` LIKE '"+Nombre+"'");
                        System.out.println("Ingrese el numero de unidades a vender");
                        unidades = Lector.nextInt();                       
                        resultado.next();
                        Uactuales= resultado.getInt("Iactual");
                        
                        if(unidades<= Uactuales){
                            Uactuales=Uactuales-unidades;
                            estado.executeUpdate("UPDATE `productos16`.`productos` SET `Iactual` = '"+Uactuales+"' WHERE `productos`.`id` ="+id+"");
                            System.out.println("La venta se realizo con exito");
                        }
                        else{
                            System.out.println("La venta no se pudo realizar por que no hay suficientes unidades en el inventario");
                        }
                    }                            
                break;
                case 6: 
                    resultado = estado.executeQuery("SELECT * FROM `productos`");
                    System.out.println("ID"+"\t"+"Nombre"+"\t"+"Cantidad Vendida"+"\t"+"Ganancia");
                    Nprecio=0;
                    while(resultado.next()){
                         unidades = resultado.getInt("Iinicial"); 
                         Uactuales= resultado.getInt("Iactual");
                         unidades= unidades-Uactuales;
                         Precio = resultado.getInt("Precio");
                         Precio= Precio*unidades;
                         Nprecio=Nprecio+Precio;
                         System.out.println(resultado.getString("id")+"\t"+resultado.getString("Nombre")+"\t"+"\t"+unidades+"\t"+"\t"+Precio);                   
                    }
                    System.out.println("Ganancias Totales: "+"\t"+"\t"+"\t"+Nprecio);
                break;    
                    
                case 7: 
                    N=1;
                break;    
            }
        }
        

 
        }catch (SQLException ex){
            System.out.println("Error de mysql");// Error de sintaxis!!!!!!!!!!!!!!!!!!!!
        }catch(Exception e){ // no se tiene certeza cual es el error que se cometio para eso esta  +e.getMessage()
            System.out.println("Se ha encontrado un error de tipo: "+e.getMessage());
        }
    }
    
}
