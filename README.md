# portfolioPDFGenerator

This project uses Springboot as a main framework to execute the application to read information from the GPP and convert it to pdf.

**Main class**

PortfolioGeneratorApplication --> This class is the one to be executed to launch the application.

**Resources**

path: src/main/resources/postman This path contains the JSON file with the collection of services for the app


**Services**

| Service       | Type | URL                                                                                   | Parameters                                                                                                                                                                                          |
|---------------|------|---------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| upload        | POST | http://**host:port**/documents/upload                                                 | **file:** The file to be uploaded                                                                                                                                                                   |
| parseDocument | POST | http://**host:port**/documents/parseDocument/{document_name}?week=[int]&format=[type] | **week:** Number of the week to be analyzed <br> **format:** Type of the output files of the process, accepted values [docx,pdf]                                                                    |
| sendEmail     | POST | http://**host:port**/documents/sendEmail?week=[int]&year=[int]                        | **week:** Number of the week to be analyzed <br> **year:** Current year                                                                                                                             |
| doAll         | POST | http://**host:port**/documents/doAll?week=[int]&year=[int]&format=[type]              | **week:** Number of the week to be analyzed <br> **year:** Current year <br> **file:** The file to be uploaded <br> **format:** Type of the output files of the process, accepted values [docx,pdf] |



