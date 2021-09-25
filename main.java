import java.lang.reflect.Array;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class main {
  static class Funcionario
  {
    public int sindicato;
    public String id_sindicato;
    public double taxa_sindicato;
    public Calendar ultimaEntrada;
    public String nome, endereco, taxas_nome[];
    public int tipo,horas_trabalhadas[][];// 1-horista 2-assalariado 3-comissionado
    public double salario, salario_mensal,salario_hora, comissao,vendas[][],taxas[];
    public int dia_pag,tipo_pag; //1-semanal 2-bissemanal 3-mensal 

    public Funcionario(){
      sindicato = 0;
      horas_trabalhadas = new int[12][31];
      vendas = new double[12][31];
      taxas_nome = new String[10];
      taxas = new double[10];
      dia_pag =6;
      tipo_pag=1;
    }
  };
  
  static class Agenda{
    public int dia,tipo;
  };

  static Scanner scan = new Scanner(System.in);
  static Calendar calendar = Calendar.getInstance();
  static String data= ""+calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);;


  public static void main(String[] args) {
    Funcionario funcionarios[] = new Funcionario[20];
    Funcionario ultimaAlteracao[] = new Funcionario[20];
    Agenda agenda[] = new Agenda[20];
    
    setarFuncionariosIniciais(funcionarios);
    int totalfuncionarios = 0;
    int opcao=0;
    do{
      opcao = menu();
      switch(opcao){
      case 1:
        undoRedo(ultimaAlteracao,funcionarios);
        if(adicionarFuncionario(funcionarios)==1)
          totalfuncionarios++;
        break;
      case 2:
        if(totalfuncionarios>0){
          undoRedo(ultimaAlteracao,funcionarios);
          if(removerFuncionario(funcionarios,totalfuncionarios)==1)
            totalfuncionarios--;
        }
        else
          System.out.println("Nao tem funcionarios cadastrados");
        break;
      case 3:
        undoRedo(ultimaAlteracao,funcionarios);
        lancarCartaoPonto(funcionarios);
        break;
      case 4:
        undoRedo(ultimaAlteracao,funcionarios);
        baterPonto(funcionarios);
        break;
      case 5:
        undoRedo(ultimaAlteracao,funcionarios);
        resultadoVenda(funcionarios);
        break;
      case 6:
        undoRedo(ultimaAlteracao,funcionarios);
        lancarTaxaDeServico(funcionarios);
        break;
      case 7:
        undoRedo(ultimaAlteracao,funcionarios);
        alterarFuncionario(funcionarios);
        break;
      case 8:
        gerarFolhaPagamento(funcionarios);
        break;
      case 9:
        Funcionario[] aux = new Funcionario[20];
        for(int i=0;i<20;i++){
          aux[i] = ultimaAlteracao[i];
          ultimaAlteracao[i] = funcionarios[i];
          funcionarios[i] = aux[i];
        }
        break;
      case 10:
        undoRedo(ultimaAlteracao,funcionarios);
        criarAgendas(funcionarios,agenda);
        break;
      case 11:
        listarFuncionariosDetalhe(funcionarios);
        break;
      case 12:
        System.out.println("Saiu do sistema");
        break;
      default:
        System.out.println("Essa opcao nao existe!");
        break;
      }
    }while(opcao!=12);
  }
  
  private static void criarAgendas(Funcionario[] funcionarios,Agenda[] agenda) {
    System.out.println("----Agenda de pagamentos----");
    for(int i=0;i<10;i++){
      if(agenda[i]!=null){
        System.out.print("Agenda "+i+":\nTipo de pagamento: ");
        if(agenda[i].tipo==1)
          System.out.println("Semanal "+diaPagamento(agenda[i].dia));
        if(agenda[i].tipo==2)
          System.out.println("Bissemanal "+diaPagamento(agenda[i].dia));
        if(agenda[i].tipo==3) 
          System.out.println("Mensal "+agenda[i].dia);

      }
    }
    
    System.out.println("Digite uma opcao:\n1-Criar nova agenda\n2-Associar agenda");
    int op = scan.nextInt();
    if(op==1){
      for(int i=0;i<10;i++){
        if(agenda[i]==null){
          agenda[i] = new Agenda();
          System.out.println("Digite o tipo de pagamento: 1-semanal 2-bissemanal 3-mensal");
          agenda[i].tipo = scan.nextInt();
          if(agenda[i].tipo==3)
            System.out.println("Digite o dia do mes");
          else
            System.out.println("Digite o dia da semana");
          agenda[i].dia = scan.nextInt();
          
          System.out.println("Criada com sucesso!");
          break;
        }
      }
    }else if(op==2){
      System.out.println("Digite o numero da agenda: ");
      int idAgenda = scan.nextInt();
      listarFuncionariosPorNome(funcionarios);
      System.out.println("Digite o numero do funcionario: ");
      int id = scan.nextInt();
      
      funcionarios[id].dia_pag = agenda[idAgenda].dia;
      funcionarios[id].tipo_pag = agenda[idAgenda].tipo;
      
      System.out.println("Associado com sucesso!");
    }
    
  }

  private static void undoRedo(Funcionario[] ultimaAlteracao, Funcionario[] funcionarios) {
    for(int i=0;i<20;i++){
      if(funcionarios[i]!=null){
        if(ultimaAlteracao[i]==null)
          ultimaAlteracao[i] = new Funcionario();
      ultimaAlteracao[i].nome = funcionarios[i].nome;
      ultimaAlteracao[i].endereco = funcionarios[i].endereco;
      ultimaAlteracao[i].sindicato = funcionarios[i].sindicato;
      ultimaAlteracao[i].id_sindicato = funcionarios[i].id_sindicato;
      ultimaAlteracao[i].taxa_sindicato = funcionarios[i].taxa_sindicato;
      ultimaAlteracao[i].ultimaEntrada = funcionarios[i].ultimaEntrada;
      ultimaAlteracao[i].taxas_nome = funcionarios[i].taxas_nome;
      ultimaAlteracao[i].tipo = funcionarios[i].tipo;
      ultimaAlteracao[i].horas_trabalhadas = funcionarios[i].horas_trabalhadas;
      ultimaAlteracao[i].salario = funcionarios[i].salario;
      ultimaAlteracao[i].salario_mensal = funcionarios[i].salario_mensal;
      ultimaAlteracao[i].salario_hora = funcionarios[i].salario_hora;
      ultimaAlteracao[i].comissao = funcionarios[i].comissao;
      ultimaAlteracao[i].vendas = funcionarios[i].vendas;
      ultimaAlteracao[i].taxas = funcionarios[i].taxas;
      ultimaAlteracao[i].tipo_pag = funcionarios[i].tipo_pag;
      }
    }
  }
  
  
  
  public static void setarFuncionariosIniciais(Funcionario[] func) {
  }

  private static void gerarFolhaPagamento(Funcionario[] func) {
    scan.nextLine();
    System.out.print("Digite a data da folha: ");
    String datafolha =scan.nextLine();
    for(int i=0;i<20;i++){
      if(func[i]!=null){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        try {
          cal.setTime(sdf.parse(datafolha));
        } catch (ParseException e) {
          e.printStackTrace();
        }
        System.out.println("func:"+func[i].nome+ " - "+func[i].tipo_pag+ " - "+func[i].dia_pag + " - "+cal.get(Calendar.DAY_OF_MONTH));
        if(func[i].tipo_pag == 3 && func[i].dia_pag==cal.get(Calendar.DAY_OF_MONTH)){
          pagarFuncionario(func[i],cal);
        }else if(func[i].tipo_pag==1 && func[i].dia_pag==cal.get(Calendar.DAY_OF_WEEK))
          pagarFuncionario(func[i],cal);
        else if(func[i].tipo_pag==2 && func[i].dia_pag==cal.get(Calendar.DAY_OF_WEEK) && cal.get(Calendar.WEEK_OF_YEAR)%2==0){
          pagarFuncionario(func[i],cal);
        }
        
      }
    }

  }

  private static void pagarFuncionario(Funcionario func,Calendar cal) {
    System.out.println("Funcionario: "+func.nome);
    Calendar aux = cal;
    System.out.println("dia:"+aux.get(Calendar.DAY_OF_MONTH));
    System.out.println("mes"+aux.get(Calendar.MONTH));
    double salario_comissao=0;
    func.salario=0.0;
    if(func.tipo==1){
      int j=0;
      if(func.tipo_pag==1){
        j=7;
      }else if(func.tipo_pag==2){
        j=14;
      }else if(func.tipo_pag==3){
        aux.add(Calendar.MONTH, -1);
        j=aux.getActualMaximum(Calendar.DAY_OF_MONTH);
      }
      aux = cal;
      for(int i=0;i<j;i++){
        aux.add(Calendar.DAY_OF_YEAR, -1);
        double horas = (func.horas_trabalhadas[aux.get(Calendar.MONTH)][aux.get(Calendar.DAY_OF_MONTH)-1]/60.0);
        if(horas>8){
          func.salario += func.salario_hora * 8;
          func.salario += func.salario_hora * 1.5 * (horas-8);
        }else{
          func.salario += func.salario_hora * horas;
        }
      }
    }else if(func.tipo==2){
      func.salario = func.salario_mensal;      
    }else if(func.tipo==3){
      func.salario = 500.0;
      int j=0;
      if(func.tipo_pag==1){
        j=7;
      }else if(func.tipo_pag==2){
        j=14;
      }else if(func.tipo_pag==3){
        aux.add(Calendar.MONTH, -1);
        j=aux.getActualMaximum(Calendar.DAY_OF_MONTH);
      }
      aux = cal;
      for(int i=0;i<j;i++){
        aux.add(Calendar.DAY_OF_YEAR, -1);
        int mes=aux.get(Calendar.MONTH),dia = aux.get(Calendar.DAY_OF_MONTH);
        double vendas = (func.vendas[mes][dia]);
        System.out.println("dia:"+dia+" mes:"+mes+ " valor: "+vendas);
        salario_comissao += vendas*(func.comissao/100);
      }
    }

    System.out.println("Salario bruto: "+func.salario);
    double taxas=0.0;
    for(int i=0;i<10;i++){
      if(func.taxas[i]!=0){
        System.out.println("Taxa "+func.taxas_nome[i]+": "+func.taxas[i]+"%");
        taxas += func.salario*(func.taxas[i]/100);
      }
    }
    if(func.sindicato==1){
      System.out.println("Taxa do sindicato:"+func.taxa_sindicato);
      taxas += func.taxa_sindicato;
    }
    
    if(func.tipo==3)
      System.out.println("Comissao de vendas: "+salario_comissao);

    System.out.println("Salario liquido: "+(func.salario-taxas+salario_comissao));
    System.out.println("-------------------------------");
  }

  private static void alterarFuncionario(Funcionario[] func) {
    listarFuncionariosPorNome(func);
    System.out.println("Digite o numero do funcionario: ");
    int id = scan.nextInt();
    if(func[id]!=null){
      detalhesFuncionario(func[id]);
      System.out.println("Digite o que voce deseja alterar: ");
      int flag=1;
      do{
        int opcao = scan.nextInt();  
        switch(opcao){
        case 1:
          System.out.println("Digite o novo nome:");
          scan.nextLine();
          func[id].nome = scan.nextLine();
          break;
        case 2:
          System.out.println("Digite o novo endereço:");
          scan.nextLine();
          func[id].endereco = scan.nextLine();
          break;
        case 3:
          System.out.println("Digite o novo tipo:");
          func[id].tipo = scan.nextInt();
          if(func[id].tipo == 1){
            System.out.println("Digite o salario-hora:");
            func[id].salario_hora = scan.nextDouble();
          }
          else if(func[id].tipo == 2){
            System.out.println("Digite o salario-mensal:");
            func[id].salario_mensal = scan.nextDouble();
          }
          else if(func[id].tipo == 3){
            System.out.println("Digite o percentual de comissao:");
            func[id].comissao = scan.nextDouble();
          }
          break;
        case 4:
          if(func[id].tipo == 1){
            System.out.println("Digite o novo salario-hora:");
            func[id].salario_hora = scan.nextDouble();
          }
          else if(func[id].tipo == 2){
            System.out.println("Digite o novo salario-mensal:");
            func[id].salario_mensal = scan.nextDouble();
          }
          else if(func[id].tipo == 3){
            System.out.println("Digite o novo percentual de comissao:");
            func[id].comissao = scan.nextDouble();
          }
          break;
        case 5:  
          System.out.println("Digite o novo tipo de pagamento:\n\t1-Semanal\n\t2-Bissemanal\n\t3-Mensal");
          func[id].tipo_pag = scan.nextInt();
          if(func[id].tipo_pag == 1 || func[id].tipo_pag == 2)
            System.out.println("Digite o dia da semana: 1-Dom 2-Seg 3-Ter 4-Quar 5-Qui 6-Sex 7-Sab");
          if(func[id].tipo_pag == 1 || func[id].tipo_pag == 3)
            System.out.println("Digite o dia do mes:");
          func[id].dia_pag = scan.nextInt();
          break;
        case 6:
          System.out.println("Digite se o funcionario faz parte do sindicato:\n1-Sim\n2-Nao");
          func[id].sindicato = scan.nextInt();
          if(func[id].sindicato==1){
            System.out.println("Digite a identificacao no sindicato : ");
            scan.nextLine();
            func[id].id_sindicato = scan.nextLine();
            System.out.println("Taxa do sindicato: "+func[id].taxa_sindicato);
            func[id].taxa_sindicato = scan.nextDouble();
          }
          break;
        default:
          System.out.println("Opcao incorreta!");
          break;  
        }
        System.out.println("\nDigite 1 para editar outra informação ou 0 pra voltar ao menu");
        flag=scan.nextInt();
      }while(flag!=0);
    }
  }

  private static void detalhesFuncionario(Funcionario func) {
    System.out.println("\n---Detalhes do funcionario---");
    System.out.println("1 - Nome: "+func.nome);
    System.out.println("2- Endereço: "+func.endereco);
    System.out.print("3- Tipo: ");
    if(func.tipo==1){
      System.out.println("Horista");
      System.out.println("4- Salario Hora:"+func.salario_hora);
    }
    if(func.tipo==2){
      System.out.println("Assalariado");
      System.out.println("4- Salario Mensal:"+func.salario_mensal);
    }
    if(func.tipo==3){
      System.out.println("Comissionado");
      System.out.println("4- Percentual comissao:"+func.comissao);
    }
    System.out.print("5- Metodo de pagamento: ");
    if(func.tipo_pag==1)
      System.out.println("   Semanal "+diaPagamento(func.dia_pag));
    if(func.tipo_pag==2)
      System.out.println("   Bissemanal "+diaPagamento(func.dia_pag));
    if(func.tipo_pag==3) 
      System.out.println("   Mensal "+func.dia_pag);

    System.out.print("6-Sindicato:");
    if(func.sindicato==1){
      System.out.println("\n   Identificacao no sindicato: "+func.id_sindicato);
      System.out.println("   Taxa do sindicato: "+func.taxa_sindicato);
    }
    else{
      System.out.println("   Nao pertence ao sindicato!");
    }

  }

  private static String diaPagamento(int dia_pag) {
    if(dia_pag==Calendar.SUNDAY)
      return "Domingo";
    if(dia_pag==Calendar.MONDAY)
      return "Segunda";
    if(dia_pag==Calendar.TUESDAY)
      return "Terça";
    if(dia_pag==Calendar.WEDNESDAY)
      return "Quarta";
    if(dia_pag==Calendar.THURSDAY)
      return "Quinta";
    if(dia_pag==Calendar.FRIDAY)
      return "Sexta";
    if(dia_pag==Calendar.SATURDAY)
      return "Sábado";

    return null;
  }

  private static void lancarTaxaDeServico(Funcionario[] func) {
    listarFuncionariosPorNome(func);
    System.out.println("Digite o numero do funcionario: ");
    int id = scan.nextInt();    
    scan.nextLine();
    System.out.println("Digite o nome da taxa: ");
    String nomeTaxa = scan.nextLine();  
    System.out.println("Digite o valor da taxa em porcentagem: ");
    double taxa = scan.nextDouble();  
    for(int i=0;i<10;i++){
      if(func[id].taxas[i]==0){
        func[id].taxas[i] = taxa;
        func[id].taxas_nome[i] = nomeTaxa;
        System.out.println("Taxa cadastrada com sucesso!");
        break;
      }
    }
  }

  private static void resultadoVenda(Funcionario[] func) {
    listarFuncionariosPorNome(func);
    System.out.println("Digite o numero do funcionario: ");
    int id = scan.nextInt();

    System.out.println("Voce deseja:\n\t1-Ver resultado de vendas\n\t2-Lançar venda\n");
    int opc = scan.nextInt();
    if(opc==1){
      System.out.print("Digite o mes: ");
      int mes = scan.nextInt();
      imprimirVendas(func[id],mes);
    }else if (opc==2){
      System.out.print("Digite o valor da venda: ");
      double valor = scan.nextDouble();
      System.out.print("Digite o mes da venda: ");
      int mes = scan.nextInt();
      System.out.print("Digite o dia da venda: ");
      int dia = scan.nextInt();
      func[id].vendas[mes-1][dia-1] += valor;

      System.out.println("Venda cadastrada!");
    }
  }

  private static void imprimirVendas(Funcionario funcionario, int mes) {
    System.out.println("----Resultado vendas mes "+mes+" - "+funcionario.nome+"----");
    for(int i=0;i<31;i++){
      if(funcionario.vendas[mes-1][i]!=0)
        System.out.println("Dia "+(i+1)+":"+funcionario.vendas[mes-1][i]+" reais.");
    }

  }

  public static int menu (){
    System.out.println("\n---- Menu Folha de Pagamento ----   "+data+" \n"
        + "1- Adição de um empregado\n"
        + "2- Remoção de um empregado\n"
        + "3- Lançar um Cartão de Ponto\n"
        + "4- Assinar ponto do funcionario\n"
        + "5- Lançar um Resultado Venda\n"
        + "6- Lançar uma taxa de serviço\n"
        + "7- Alterar detalhes de um empregado\n"
        + "8- Rodar a folha de pagamento para hoje\n"
        + "9- Undo\\Redo\n"
        + "10- Agenda de pagamento\n"
        + "11- Listar funcionarios\n"
        + "12- Sair\n");
    return scan.nextInt();
  }

  private static void baterPonto(Funcionario func[]) {
    listarFuncionariosPorNome(func);
    System.out.println("Digite o numero do funcionario: ");
    int id = scan.nextInt();
    scan.nextLine();
    System.out.println("Digite a hora:");
    String hora = scan.nextLine();
    String dataEHora = data + " " + hora;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    Calendar cal = Calendar.getInstance();
    try {
      cal.setTime(sdf.parse(dataEHora));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if(func[id].ultimaEntrada==null){
      System.out.println("Entrada batida: "+cal.get(Calendar.HOUR_OF_DAY) + ":"+cal.get(Calendar.MINUTE));
      func[id].ultimaEntrada = cal;
    }
    else{
      Calendar calentrada = func[id].ultimaEntrada;
      System.out.println("Saida batida: "+cal.get(Calendar.HOUR_OF_DAY) + ":"+cal.get(Calendar.MINUTE));

      long minutos = (cal.getTimeInMillis() - calentrada.getTimeInMillis())/60000;
      System.out.println("min"+minutos);
      func[id].horas_trabalhadas[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)]=(int) minutos;
      func[id].ultimaEntrada=null;
    } 
  }
  public static void imprimirFuncionario(Funcionario func){
    System.out.println("--Funcionario--");
    System.out.println("Nome: "+func.nome);
    System.out.println("Endereco: "+func.endereco);
    System.out.println("Tipo: "+func.tipo);
    System.out.println("Salario: "+func.salario);
    System.out.println("Salario mensal: "+func.salario_mensal);
    System.out.println("Comissao: "+func.comissao);
  }

  public static int adicionarFuncionario(Funcionario func[]){
    int j=0;
    for(int i=0;i<20;i++){
      if(func[i]==null){
        j=i;
        break;
      }
    }

    System.out.println("Adicionar funcionario");
    func[j] = new Funcionario();
    System.out.println("Nome: ");
    scan.nextLine();
    func[j].nome = scan.nextLine();
    System.out.println("Endereco: ");
    func[j].endereco = scan.nextLine();
    System.out.println("Tipo: 1-horista 2-assalariado 3-comissionado ");
    int tipo = scan.nextInt();
    func[j].tipo = tipo;
    if(tipo==1){
      System.out.println("Salario hora");
      func[j].salario = scan.nextDouble();
    }else if(tipo==2){
      System.out.println("Salario Mensal: ");
      func[j].salario_mensal = scan.nextDouble();
    }else if(tipo==3){
      System.out.println("Porcentagem da comissao: ");
      func[j].comissao = scan.nextDouble();
    }
    return 1;
  }

  public static void listarFuncionariosPorNome(Funcionario func[]){
    System.out.println("\nLista de funcionarios:");
    for( int i = 0 ; i < func.length ; i++ ){
      if(func[i]!=null){
        System.out.println("Funcionario "+i+" - Nome: "+func[i].nome);
      }
    }
    System.out.println("");
  }
  
  public static void listarFuncionariosDetalhe(Funcionario func[]){
    System.out.println("\nLista de funcionarios:");
    for( int i = 0 ; i < func.length ; i++ ){
      if(func[i]!=null){
        detalhesFuncionario(func[i]);
      }
    }
    System.out.println("");
  }
  

  public static int removerFuncionario(Funcionario func[],int totalfuncionarios){
    listarFuncionariosPorNome(func);
    System.out.println("Digite o numero do funcionario a ser removido: ");
    int id = scan.nextInt();
    if(func[id]!=null){
      func[id]=null;
      return 1;
    }else{
      return 0;
    }
  }

  private static void lancarCartaoPonto(Funcionario func[]) {
    listarFuncionariosPorNome(func);
    System.out.println("Digite o numero do funcionario: ");
    int id = scan.nextInt();
    System.out.println("Digite o mes(1-12): ");
    int mes = scan.nextInt();
    if(func[id]!=null){
      imprimirDiasTrabalhados(func[id],mes);
    }
  }

  private static void imprimirDiasTrabalhados(Funcionario funcionario, int mes) {
    System.out.println("----Dias trabalhados mes "+mes+"----");
    for(int i=0;i<31;i++){
      if(funcionario.horas_trabalhadas[mes][i]!=0)
        System.out.println("Dia "+i+":"+funcionario.horas_trabalhadas[mes][i]+" minutos");
    }
  }


}
