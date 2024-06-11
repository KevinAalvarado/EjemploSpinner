package bryan.miranda.ejemplospinner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.dataClassDoctores

class pacientes : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pacientes, container, false)

        //Mando a llamar a el spinner para programarlo
        val spDoctores = root.findViewById<Spinner>(R.id.spDoctores)

        //1- Creamos la funcion que haga un select
        fun obtenerDoctores(): List<dataClassDoctores> {
            //Creo un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbDoctores")!!

            val listaDoctores = mutableListOf<dataClassDoctores>()

            while (resultSet.next()){
                val uuid = resultSet.getString("DoctorUUID")
                val nombre = resultSet.getString("nombreDoctor")
                val especialidad = resultSet.getString("Especialidad")
                val telefono = resultSet.getString("Telefono")
                val unDOctorCompleto = dataClassDoctores(uuid, nombre, especialidad, telefono)
                listaDoctores.add(unDOctorCompleto)
            }
            return listaDoctores
        }

        //ultimo punto programar el spinner
        CoroutineScope(Dispatchers.IO).launch {
            //1- obtengo el listado de datos a mostrar
            val listadoDoctores = obtenerDoctores()
            val nombreDoctores = listadoDoctores.map { it.nombreDoctor }
            withContext(Dispatchers.Main){
                val miAdaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombreDoctores)
                spDoctores.adapter = miAdaptador
            }
        }


        return root
    }
}