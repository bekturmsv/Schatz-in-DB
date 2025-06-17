package pti.softwareentwicklg.SchatzInDb.utils.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.task.StudyMaterial;
import pti.softwareentwicklg.SchatzInDb.repository.task.StudyMaterialRepository;

import java.util.List;

@Component
public class StudyMaterialInitializer {

    private final StudyMaterialRepository studyMaterialRepository;

    public StudyMaterialInitializer(StudyMaterialRepository studyMaterialRepository) {
        this.studyMaterialRepository = studyMaterialRepository;
    }

    @PostConstruct
    public void initStudyMaterials() {
        List<StudyMaterial> materials = List.of(
                createMaterial(
                        "The SQL SELECT Statement",
                        """
                                    The SELECT statement is used to select data from a database.
                            
                                    Example:
                                    ```sql
                                    SELECT CustomerName, City FROM Customers;
                                    ```
                            
                                    Syntax:
                                    ```sql
                                    SELECT column1, column2, ...
                                    FROM table_name;
                                    ```
                                    Here, column1, column2, ... are the field names of the table you want to select data from.
                                    The table_name represents the name of the table you want to select data from.
                                  """,
                        SqlKategorie.SELECT,
                        null
                )
        );

        studyMaterialRepository.saveAll(materials);
    }

    private StudyMaterial createMaterial(String title, String description, SqlKategorie sqlKategorie, Long teacherId) {
        StudyMaterial material = new StudyMaterial();
        material.setTitle(title);
        material.setDescription(description);
        material.setSqlKategorie(sqlKategorie);
        material.setTeacher(teacherId);
        return material;
    }
}