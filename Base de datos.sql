-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 17-12-2024 a las 02:52:38
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `articulos`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `acta_edicion`
--

CREATE TABLE `acta_edicion` (
  `id_acta` int(11) NOT NULL,
  `nombre` varchar(40) DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `id_ciudad` int(11) DEFAULT NULL,
  `id_congreso` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `acta_edicion`
--

INSERT INTO `acta_edicion` (`id_acta`, `nombre`, `fecha_inicio`, `fecha_fin`, `id_ciudad`, `id_congreso`) VALUES
(1, 'Acta del Congreso Internacional de Tecno', '2024-01-15', '2024-01-16', 30, 1),
(2, 'Acta del Congreso Nacional de Sostenibil', '2024-02-20', '2024-02-21', 7, 2),
(3, 'Acta del Congreso de Inteligencia Artifi', '2024-03-10', '2024-03-11', 13, 3),
(4, 'Acta sobre Innovación y Desarrollo', '2024-04-05', '2024-04-06', 2, 4),
(5, 'Acta del Congreso de Ciencias Sociales', '2024-05-18', '2024-05-19', 8, 5),
(6, 'Acta del Congreso Internacional de Educa', '2024-06-25', '2024-06-26', 14, 1),
(7, 'Acta del Congreso de Robótica y Automati', '2024-07-30', '2024-07-31', 3, 6),
(8, 'Acta sobre Blockchain y Criptomonedas', '2024-08-12', '2024-08-13', 15, 7),
(9, 'Acta Mundial de Big Data', '2024-09-22', '2024-09-23', 5, 8),
(10, 'Acta del Congreso de Desarrollo Urbano S', '2024-10-14', '2024-10-15', 9, 9),
(11, 'Acta sobre Ciberseguridad Global', '2024-11-01', '2024-11-02', 10, 10),
(12, 'Acta del Congreso Internacional de Salud', '2024-11-20', '2024-11-21', 11, 2),
(13, 'Acta sobre Energías Renovables y Eficien', '2024-12-05', '2024-12-06', 12, 3),
(14, 'Acta del Congreso de Marketing Digital A', '2024-12-15', '2024-12-16', 6, 5),
(15, 'Acta sobre Ética y Tecnología en el Sigl', '2025-01-10', '2025-01-11', 18, 1),
(16, 'COCO1', '2024-12-01', '2024-12-03', 2, 11);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `articulo`
--

CREATE TABLE `articulo` (
  `id_articulo` int(11) NOT NULL,
  `titulo` varchar(100) DEFAULT NULL,
  `nombre_corto` varchar(40) DEFAULT NULL,
  `correo` varchar(40) DEFAULT NULL,
  `localizacion` varchar(100) DEFAULT NULL,
  `url` varchar(400) DEFAULT NULL,
  `tipo_articulo` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `articulo`
--

INSERT INTO `articulo` (`id_articulo`, `titulo`, `nombre_corto`, `correo`, `localizacion`, `url`, `tipo_articulo`) VALUES
(1, 'El impacto de las nuevas tecnologías en la educación en valores del siglo XXI.', 'Tecnología Educación', 'peres@gmail.com', 'Bogotá, Colombia', 'https://www.scielo.org.mx/scielo.php?pid=S1665-109X2011000200002&script=sci_abstract&tlng=pt', 2),
(2, 'Las ciudades hacia el desarrollo sostenible', 'Sostenibilidad Urbana', 'mgarcia@hotmail.com', 'Madrid, España', 'https://www.jstor.org/stable/20797160', 2),
(3, 'Avances en inteligencia artificial y su impacto en la sociedad', 'Inteligencia Artificial', 'akhan@gmail.com', 'Londres, Reino Unido', 'https://repository.upb.edu.co/handle/20.500.11912/4942', 2),
(4, 'Las energías renovables en la actividad turística. Innovaciones hacia la sostenibilidad', 'Energías Renovables', 'lfernandez@hotmail.com', 'Buenos Aires, Argentina', 'http://scielo.senescyt.gob.ec/scielo.php?pid=S2477-88502015000100086&script=sci_arttext', 2),
(5, 'El análisis de datos, una ciencia para la toma de decisiones', 'Análisis de Datos', 'clopez@gmail.com', 'Santiago, Chile', 'https://repository.unimilitar.edu.co/items/a82eb996-1c8d-4245-a02f-4f88489a7dd4', 3),
(6, 'Desarrollo Sostenible del Agro-Turismo en comunidades rurales de carácter agrícola', 'Desarrollo Sostenible', 'emmawhite@hotmail.com', 'Ciudad de México, México', 'https://dialnet.unirioja.es/servlet/articulo?codigo=7354261', 3),
(7, 'Transformación digital en las empresas: una revisión conceptual', 'Transformación Digital', 'dkim@gmail.com', 'Seúl, Corea del Sur', 'https://revistas.utb.edu.ec/index.php/sr/article/view/2804', 3),
(8, 'Acceso abierto a los datos de investigación, una vía hacia la colaboración científica', 'Colaboración Científica', 'smartinez@hotmail.com', 'Valencia, España', 'https://riunet.upv.es/handle/10251/90748', 3),
(9, 'El marketing digital herramientas y tendencias actuales', 'Marketing Digital', 'mrossi@gmail.com', 'Roma, Italia', 'https://dialnet.unirioja.es/servlet/articulo?codigo=8383788', 3),
(10, 'Retos de seguridad/ciberseguridad en el 2030', 'Ciberseguridad', 'anivanova@hotmail.com', 'Moscú, Rusia', 'https://sistemas.acis.org.co/index.php/sistemas/article/view/94', 1),
(11, 'Desarrollo personal y social en los años de la Educación Infantil', 'Educación Personal', 'lchen@gmail.com', 'Pekín, China', 'https://accedacris.ulpgc.es/handle/10553/5041', 1),
(12, 'Economía Circular Una Revisión desde los Modelos de Negocios y la Responsabilidad Social Empresarial', 'Economía Circular', 'psantos@gmail.com', 'Lisboa, Portugal', 'https://dialnet.unirioja.es/servlet/articulo?codigo=8890603', 1),
(13, 'La psicología del consumidor: una discusión de su estado actual y aportes al mercadeo', 'Psicología Consumidor', 'npatel@gmail.com', 'Nueva Delhi, India', 'http://publicaciones.konradlorenz.edu.co/index.php/sumapsi/article/view/294', 1),
(14, 'Técnicas y métodos en Investigación cualitativa', 'Investigación Cualitativa', 'omuller@gmail.com', 'Berlín, Alemania', 'https://ruc.udc.es/dspace/handle/2183/8533', 1),
(15, 'Estrategias para fortalecer capacidades de innovación: una visión desde micro y pequeñas empresas', 'Innovación Empresarial', 'cruiz@gmail.com', 'Barcelona, España', 'https://www.scielo.org.ar/scielo.php?pid=S1851-17162016000200009&script=sci_arttext', 1),
(17, 'COCO1 De Informe Tecnico', 'COCO1 AUX de informe Tecnico', 'COCO1@gmail.com', 'un lugar muy muy lejano', 'https://www.youtube.com', 1),
(18, 'COCO2 de Congreso', 'COCO2 AUX de Congreso', 'COCO2@gmail.com', 'un lugar misterioso', 'https://m.youtube.com/', 2),
(23, 'COCO3 de Revista', 'COCO3 AUX de Revista', 'COCO3@gmail.com', 'ya ni se ', 'https://es.m.wikipedia.org/wiki/Wikipedia:Portada', 1),
(26, 'COCO4 de Informe Tecnico', 'COCO4 AUX de Informe Técnico', 'COCO4@gmail.com', 'me lo inventé', 'https://pokeapi.co/#google_vignette', 3),
(29, 'prueba builder', 'prueba b', 'heiderleyton22@gmail.com', 'bfjd', 'ufbdb', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `articulo_acta`
--

CREATE TABLE `articulo_acta` (
  `id_articulo` int(11) NOT NULL,
  `id_acta` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `articulo_acta`
--

INSERT INTO `articulo_acta` (`id_articulo`, `id_acta`) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 1),
(18, 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `articulo_edicion`
--

CREATE TABLE `articulo_edicion` (
  `id_articulo` int(11) NOT NULL,
  `numero_edicion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `articulo_edicion`
--

INSERT INTO `articulo_edicion` (`id_articulo`, `numero_edicion`) VALUES
(10, 1),
(11, 3),
(12, 4),
(13, 6),
(14, 7),
(15, 9),
(17, 16),
(23, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `articulo_informe_tecnico`
--

CREATE TABLE `articulo_informe_tecnico` (
  `id_articulo` int(11) NOT NULL,
  `numero_informe` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `articulo_informe_tecnico`
--

INSERT INTO `articulo_informe_tecnico` (`id_articulo`, `numero_informe`) VALUES
(5, 3),
(6, 4),
(7, 5),
(8, 6),
(9, 7),
(26, 2),
(29, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `articulo_investigador`
--

CREATE TABLE `articulo_investigador` (
  `id_articulo` int(11) NOT NULL,
  `id_investigador` int(11) NOT NULL,
  `tipo_autor` varchar(60) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `articulo_investigador`
--

INSERT INTO `articulo_investigador` (`id_articulo`, `id_investigador`, `tipo_autor`) VALUES
(1, 1, 'Autor Principal'),
(1, 2, 'Coautor'),
(2, 3, 'Autor Principal'),
(2, 4, 'Coautor'),
(3, 5, 'Autor Principal'),
(3, 6, 'Coautor'),
(4, 7, 'Autor Principal'),
(5, 8, 'Coautor'),
(5, 9, 'Autor Principal'),
(6, 10, 'Coautor'),
(7, 11, 'Autor Principal'),
(8, 12, 'Coautor'),
(9, 13, 'Autor Principal'),
(10, 14, 'Coautor'),
(11, 15, 'Autor Principal'),
(13, 1, 'Autor Principal'),
(17, 6, 'Autor Principal'),
(18, 4, 'Autor Principal'),
(23, 5, 'Autor Principal'),
(26, 6, 'Autor Principal'),
(29, 4, 'Autor Principal');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `centro`
--

CREATE TABLE `centro` (
  `id_centro` int(11) NOT NULL,
  `nombre` varchar(40) DEFAULT NULL,
  `id_ciudad` int(11) DEFAULT NULL,
  `id_tema` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `centro`
--

INSERT INTO `centro` (`id_centro`, `nombre`, `id_ciudad`, `id_tema`) VALUES
(1, 'Centro de Investigación Científica', 1, 1),
(2, 'Instituto de Estudios Avanzados', 2, 2),
(3, 'Centro de Innovación y Desarrollo', 36, 3),
(4, 'Instituto de Tecnología Aplicada', 1, 4),
(5, 'Centro de Estudios Sociales', 4, 5),
(6, 'Instituto de Biomedicina', 5, 6),
(7, 'Centro de Estudios Ambientales', 6, 7),
(8, 'Instituto de Física y Química', 3, 8),
(9, 'Centro de Investigaciones Agropecuarias', 7, 9),
(10, 'Instituto de Matemáticas y Computación', 2, 10),
(11, 'Centro de Estudios Culturales', 1, 11),
(12, 'Instituto de Neurociencia', 4, 12),
(13, 'Centro de Innovación en Educación', 5, 13),
(14, 'Instituto de Seguridad y Salud', 6, 14),
(15, 'Centro de Investigación en Energía', 3, 15),
(16, 'COCO', 9, 6),
(17, 'COCO2', 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ciudad`
--

CREATE TABLE `ciudad` (
  `id_ciudad` int(11) NOT NULL,
  `ciudad` varchar(40) DEFAULT NULL,
  `id_pais` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ciudad`
--

INSERT INTO `ciudad` (`id_ciudad`, `ciudad`, `id_pais`) VALUES
(1, 'Ciudad de México', 1),
(2, 'Guadalajara', 1),
(3, 'Monterrey', 1),
(4, 'Nueva York', 2),
(5, 'Los Ángeles', 2),
(6, 'Toronto', 3),
(7, 'Vancouver', 3),
(8, 'Buenos Aires', 4),
(9, 'Córdoba', 4),
(10, 'Madrid', 5),
(11, 'Barcelona', 5),
(12, 'Río de Janeiro', 6),
(13, 'São Paulo', 6),
(14, 'Bogotá', 7),
(15, 'Medellín', 7),
(16, 'Santiago', 8),
(17, 'Valparaíso', 8),
(18, 'Lima', 9),
(19, 'Cusco', 9),
(20, 'Roma', 10),
(21, 'Milán', 10),
(22, 'París', 11),
(23, 'Marsella', 11),
(24, 'Berlín', 12),
(25, 'Múnich', 12),
(26, 'Londres', 13),
(27, 'Manchester', 13),
(28, 'Tokio', 14),
(29, 'Osaka', 14),
(30, 'Sídney', 15),
(31, 'Melbourne', 15),
(32, 'Nueva Delhi', 16),
(33, 'Mumbai', 16),
(34, 'Pekín', 17),
(35, 'Shanghái', 17),
(36, 'Moscú', 18),
(37, 'San Petersburgo', 18),
(38, 'Ciudad del Cabo', 19),
(39, 'Johannesburgo', 19),
(40, 'El Cairo', 20),
(41, 'Alejandría', 20);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `congreso`
--

CREATE TABLE `congreso` (
  `id_congreso` int(11) NOT NULL,
  `nombre` varchar(40) DEFAULT NULL,
  `tipo` int(11) DEFAULT NULL,
  `frecuencia` int(11) DEFAULT NULL,
  `primer_congreso` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `congreso`
--

INSERT INTO `congreso` (`id_congreso`, `nombre`, `tipo`, `frecuencia`, `primer_congreso`) VALUES
(1, 'Annual Health Conference', 6, 1, '1999-05-12'),
(2, 'Climate Symposium', 2, 2, '2003-09-05'),
(3, 'Water Research Summit', 3, 1, '2007-06-18'),
(4, 'Genomics Innovations', 4, 2, '2011-03-23'),
(5, 'Education Innovation Forum', 5, 1, '2005-11-01'),
(6, 'Artificial Intelligence Summit', 6, 2, '2010-04-15'),
(7, 'Sustainable Energy Expo', 7, 1, '2012-10-10'),
(8, 'International Cybersecurity Conference', 8, 3, '2015-07-22'),
(9, 'Marine Biology Symposium', 3, 2, '2008-08-30'),
(10, 'Urban Development Forum', 1, 1, '2006-12-05'),
(11, 'COCO1', 2, 1, '2024-08-06'),
(12, 'COCO2', 2, 1, '2022-05-12'),
(13, 'COCO3', 7, 25, '2024-11-03');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `edicion`
--

CREATE TABLE `edicion` (
  `numero_edicion` int(11) NOT NULL,
  `titulo` varchar(40) DEFAULT NULL,
  `editor` varchar(40) DEFAULT NULL,
  `pagina_inicio` int(11) DEFAULT NULL,
  `pagina_final` int(11) DEFAULT NULL,
  `id_revista` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `edicion`
--

INSERT INTO `edicion` (`numero_edicion`, `titulo`, `editor`, `pagina_inicio`, `pagina_final`, `id_revista`) VALUES
(1, 'Edición Especial sobre Inteligencia Arti', 'Juan Pérez', 1, 20, 1),
(2, 'Avances en Aprendizaje Automático', 'María García', 21, 40, 2),
(3, 'Tendencias en Big Data', 'Carlos López', 41, 60, 3),
(4, 'Ciencia de Datos: Nuevas Perspectivas', 'Ana Gómez', 61, 80, 4),
(5, 'Desarrollo Web y Nuevas Tecnologías', 'Luis Fernández', 81, 100, 5),
(6, 'Seguridad Informática en la Era Digital', 'Sofía Martínez', 101, 120, 6),
(7, 'Blockchain y su Impacto en la Economía', 'Diego Torres', 121, 140, 7),
(8, 'Internet de las Cosas: Retos y Oportunid', 'Elena Ruiz', 141, 160, 8),
(9, 'Realidad Aumentada y Virtual: Aplicacion', 'Ricardo Jiménez', 161, 180, 9),
(10, 'Computación en la Nube: El Futuro de la ', 'Patricia Castro', 181, 200, 10),
(11, 'Análisis Predictivo en Negocios Modernos', 'Fernando Morales', 201, 220, 11),
(12, 'Automatización de Procesos: Eficiencia y', 'Gabriela Salazar', 221, 240, 12),
(13, 'Robótica: Innovaciones y Desafíos Futuro', 'Andrés Ruiz', 241, 260, 13),
(14, 'Sistemas Distribuidos: Teoría y Práctica', 'Verónica Sánchez', 261, 280, 14),
(15, 'Innovación Empresarial en Tiempos de Cam', 'Olga Muller', 281, 300, 15),
(16, 'COCO1', 'COCO', 1, 2, 16),
(17, 'edición prueba', ' yop', 5, 5, 17);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `genero`
--

CREATE TABLE `genero` (
  `id_genero` int(11) NOT NULL,
  `genero` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `genero`
--

INSERT INTO `genero` (`id_genero`, `genero`) VALUES
(1, 'Femenino'),
(2, 'Masculino');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `informe_tecnico`
--

CREATE TABLE `informe_tecnico` (
  `numero_informe` int(11) NOT NULL,
  `fecha` date DEFAULT NULL,
  `tema` int(11) DEFAULT NULL,
  `id_centro` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `informe_tecnico`
--

INSERT INTO `informe_tecnico` (`numero_informe`, `fecha`, `tema`, `id_centro`) VALUES
(1, '2024-01-15', 1, 16),
(2, '2024-02-20', 2, 2),
(3, '2024-03-10', 3, 3),
(4, '2024-04-05', 4, 4),
(5, '2024-05-18', 5, 5),
(6, '2024-06-25', 6, 6),
(7, '2024-07-30', 7, 7),
(8, '2024-08-12', 8, 8),
(9, '2024-09-22', 9, 9),
(10, '2024-10-14', 10, 10),
(11, '2024-11-01', 11, 11),
(12, '2024-11-20', 12, 12),
(13, '2024-12-05', 13, 13),
(14, '2024-12-15', 14, 14),
(15, '2025-01-10', 15, 15),
(16, '2024-12-24', 1, 1),
(17, '2024-12-24', 10, 3),
(18, '2024-11-24', 3, 1),
(19, '2024-12-02', 16, 16),
(22, '2024-12-12', 16, 16),
(24, '2024-12-14', 2, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `investigador`
--

CREATE TABLE `investigador` (
  `id_investigador` int(11) NOT NULL,
  `nombre` varchar(40) DEFAULT NULL,
  `correo` varchar(70) DEFAULT NULL,
  `centro` int(11) DEFAULT NULL,
  `genero` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `investigador`
--

INSERT INTO `investigador` (`id_investigador`, `nombre`, `correo`, `centro`, `genero`) VALUES
(1, 'Laura Martínez', 'laura.martinez@gmail.com', 1, 1),
(2, 'Carlos Rodríguez', 'carlos.rodriguez@hotmail.com', 2, 2),
(3, 'Ana Góme', 'ana.gomez@yahoo.con', 3, 1),
(4, 'Juan Pérez', 'juan.perez@gmail.com', 1, 2),
(5, 'Marta López', 'marta.lopez@hotmail.com', 4, 1),
(6, 'Diego Torres', 'diego.torres@gmail.com', 5, 2),
(7, 'Elena Ruiz', 'elena.ruiz@yahoo.com', 6, 1),
(8, 'Ricardo Jiménez', 'ricardo.jimenez@gmail.com', 7, 2),
(9, 'Sofía Hernández', 'sofia.hernandez@hotmail.com', 3, 1),
(10, 'Luis Fernández', 'luis.fernandez@gmail.com', 2, 2),
(11, 'Patricia Castro', 'patricia.castro@gmail.com', 1, 1),
(12, 'Fernando Morales', 'fernando.morales@hotmail.com', 4, 2),
(13, 'Verónica Sánchez', 'veronica.sanchez@gmail.com', 5, 1),
(14, 'Andrés Ruiz', 'andres.ruiz@yahoo.com', 6, 2),
(15, 'Gabriela Salazar', 'gabriela.salazar@gmail.com', 3, 1),
(16, 'COCO1', 'coco1@gmail.com', 1, 1),
(22, 'COCO2', 'coco2@gmail.com', 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pais`
--

CREATE TABLE `pais` (
  `id_pais` int(11) NOT NULL,
  `pais` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pais`
--

INSERT INTO `pais` (`id_pais`, `pais`) VALUES
(1, 'México'),
(2, 'Estados Unidos'),
(3, 'Canadá'),
(4, 'Argentina'),
(5, 'España'),
(6, 'Brasil'),
(7, 'Colombia'),
(8, 'Chile'),
(9, 'Perú'),
(10, 'Italia'),
(11, 'Francia'),
(12, 'Alemania'),
(13, 'Reino Unido'),
(14, 'Japón'),
(15, 'Australia'),
(16, 'India'),
(17, 'China'),
(18, 'Rusia'),
(19, 'Sudáfrica'),
(20, 'Egipto');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `palabra`
--

CREATE TABLE `palabra` (
  `id_palabra` int(11) NOT NULL,
  `palabra` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `palabra`
--

INSERT INTO `palabra` (`id_palabra`, `palabra`) VALUES
(9, 'Análisis'),
(12, 'Automatización'),
(4, 'Colaboración'),
(7, 'Desarrollo'),
(3, 'Eficiencia'),
(6, 'Estrategia'),
(15, 'Implementación'),
(2, 'Innovación'),
(11, 'Integración'),
(8, 'Investigación'),
(14, 'Modelado'),
(10, 'Optimización'),
(1, 'Sostenibilidad'),
(5, 'Tecnología'),
(13, 'Transformación');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `palabra_clave`
--

CREATE TABLE `palabra_clave` (
  `id_articulo` int(11) NOT NULL,
  `id_palabra` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `palabra_clave`
--

INSERT INTO `palabra_clave` (`id_articulo`, `id_palabra`) VALUES
(1, 1),
(1, 4),
(1, 5),
(2, 6),
(3, 5),
(3, 6),
(4, 1),
(4, 8),
(5, 6),
(5, 9),
(6, 1),
(6, 13),
(7, 6),
(7, 13),
(8, 4),
(8, 8),
(9, 5),
(9, 15),
(10, 3),
(10, 6),
(11, 5),
(11, 7),
(12, 1),
(12, 10),
(13, 3),
(13, 9),
(14, 4),
(14, 8),
(15, 2),
(15, 6),
(17, 1),
(17, 4),
(18, 2),
(18, 9),
(18, 12),
(18, 13),
(18, 15),
(23, 6),
(23, 12),
(26, 5),
(26, 7),
(29, 5),
(29, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pregunta_recuperacion`
--

CREATE TABLE `pregunta_recuperacion` (
  `id_pregunta` int(11) NOT NULL,
  `pregunta` varchar(400) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pregunta_recuperacion`
--

INSERT INTO `pregunta_recuperacion` (`id_pregunta`, `pregunta`) VALUES
(3, '¿Cuál es el nombre de tu escuela primaria?'),
(6, '¿Cuál es el nombre de tu primer amor?'),
(1, '¿Cuál es el nombre de tu primer mascota?'),
(5, '¿Cuál es el segundo nombre de tu madre?'),
(7, '¿Cuál es tu libro favorito?'),
(4, '¿Cuál es tu película favorita?'),
(2, '¿En qué ciudad naciste?');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `prestamo`
--

CREATE TABLE `prestamo` (
  `id_prestamo` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_articulo` int(11) NOT NULL,
  `estado` int(11) NOT NULL,
  `fecha_prestamo` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `prestamo`
--

INSERT INTO `prestamo` (`id_prestamo`, `id_usuario`, `id_articulo`, `estado`, `fecha_prestamo`) VALUES
(1, 1, 1, 0, '2024-01-01'),
(2, 1, 2, 0, '2024-02-02'),
(3, 1, 13, 1, '2023-03-03'),
(4, 1, 17, 0, '2024-01-04'),
(5, 1, 18, 1, '2024-12-05'),
(6, 2, 3, 1, '2024-04-07'),
(7, 2, 4, 0, '2024-05-06'),
(8, 3, 5, 1, '2024-06-08'),
(9, 3, 6, 0, '2024-07-09'),
(10, 3, 14, 0, '2024-03-10'),
(11, 4, 7, 1, '2024-11-11'),
(12, 4, 8, 0, '2024-12-12'),
(13, 5, 9, 1, '2024-01-03'),
(14, 5, 10, 0, '2023-08-21'),
(15, 6, 11, 1, '2024-07-06'),
(16, 7, 12, 0, '2024-03-03'),
(17, 7, 15, 1, '2024-01-12'),
(18, 1, 17, 0, '2024-05-14'),
(19, 1, 17, 0, '2024-03-01'),
(20, 1, 17, 0, '2024-07-16'),
(21, 1, 17, 0, '2024-11-03'),
(22, 1, 17, 0, '2024-12-06'),
(23, 1, 17, 1, '2024-07-07'),
(24, 1, 18, 0, '2024-04-08'),
(25, 1, 4, 0, '2024-12-14'),
(26, 1, 4, 0, '2024-12-14');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recuperacion_contrasena`
--

CREATE TABLE `recuperacion_contrasena` (
  `id_usuario` int(11) NOT NULL,
  `id_pregunta` int(11) NOT NULL,
  `respuesta` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `recuperacion_contrasena`
--

INSERT INTO `recuperacion_contrasena` (`id_usuario`, `id_pregunta`, `respuesta`) VALUES
(1, 1, 'coco'),
(2, 4, 'El Padrino'),
(3, 5, 'María'),
(4, 7, 'Cien Años de Soledad'),
(5, 5, 'Sofía'),
(6, 5, 'Lucía'),
(7, 6, 'Andrés'),
(9, 1, 'coco'),
(10, 3, 'coco2'),
(11, 4, 'lolo'),
(14, 4, 'coco'),
(15, 7, 'lulo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `revista`
--

CREATE TABLE `revista` (
  `id_revista` int(11) NOT NULL,
  `nombre` varchar(40) DEFAULT NULL,
  `tema` int(11) DEFAULT NULL,
  `frecuencia` int(11) DEFAULT NULL,
  `primera_edicion` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `revista`
--

INSERT INTO `revista` (`id_revista`, `nombre`, `tema`, `frecuencia`, `primera_edicion`) VALUES
(1, 'Revista de Inteligencia Artificial', 1, 12, '2024-01-01'),
(2, 'Revista de Aprendizaje Automático', 2, 6, '2024-02-01'),
(3, 'Revista de Big Data', 3, 4, '2024-03-01'),
(4, 'Revista de Ciencia de Datos', 4, 3, '2024-04-01'),
(5, 'Revista de Desarrollo Web', 5, 6, '2024-05-01'),
(6, 'Revista de Seguridad Informática', 6, 12, '2024-06-01'),
(7, 'Revista de Blockchain', 7, 12, '2024-07-01'),
(8, 'Revista de Internet de las Cosas', 8, 6, '2024-08-01'),
(9, 'Revista de Realidad Aumentada', 9, 4, '2024-09-01'),
(10, 'Revista de Realidad Virtual', 10, 3, '2024-10-01'),
(11, 'Revista de Computación en la Nube', 11, 12, '2024-11-01'),
(12, 'Revista de Análisis Predictivo', 12, 6, '2024-12-01'),
(13, 'Revista de Automatización de Procesos', 13, 12, '2025-01-01'),
(14, 'Revista de Robótica', 14, 6, '2025-02-01'),
(15, 'Revista de Sistemas Distribuidos', 15, 12, '2025-03-01'),
(16, 'COCO', 21, 3, '2024-12-03'),
(17, 'COCO2', 1, 12, '2022-01-01');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tema`
--

CREATE TABLE `tema` (
  `id_tema` int(11) NOT NULL,
  `tema` varchar(60) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tema`
--

INSERT INTO `tema` (`id_tema`, `tema`) VALUES
(12, 'Análisis Predictivo'),
(2, 'Aprendizaje Automático'),
(13, 'Automatización de Procesos'),
(3, 'Big Data'),
(7, 'Blockchain'),
(4, 'Ciencia de Datos'),
(16, 'Coco'),
(17, 'COCO1'),
(19, 'COCO2'),
(21, 'COCO3'),
(23, 'COCO4'),
(24, 'COCO5'),
(25, 'COCO6'),
(11, 'Computación en la Nube'),
(5, 'Desarrollo Web'),
(1, 'Inteligencia Artificial'),
(8, 'Internet de las Cosas'),
(26, 'prueba builder'),
(27, 'prueba video'),
(9, 'Realidad Aumentada'),
(10, 'Realidad Virtual'),
(14, 'Robótica'),
(6, 'Seguridad Informática'),
(15, 'Sistemas Distribuidos');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tema_investigador`
--

CREATE TABLE `tema_investigador` (
  `id_investigador` int(11) NOT NULL,
  `id_tema` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tema_investigador`
--

INSERT INTO `tema_investigador` (`id_investigador`, `id_tema`) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(3, 5),
(3, 6),
(4, 7),
(4, 8),
(5, 9),
(5, 10),
(6, 11),
(6, 12),
(7, 13),
(7, 14),
(8, 15),
(9, 1),
(9, 2),
(10, 3),
(10, 4),
(11, 5),
(11, 6),
(12, 7),
(12, 8),
(13, 9),
(13, 10),
(14, 11),
(14, 12),
(15, 13),
(15, 14);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_articulo`
--

CREATE TABLE `tipo_articulo` (
  `id_tipo` int(11) NOT NULL,
  `tipo_articulo` varchar(60) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_articulo`
--

INSERT INTO `tipo_articulo` (`id_tipo`, `tipo_articulo`) VALUES
(2, 'Congres'),
(3, 'Informe Tecnico'),
(1, 'Revista');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_congreso`
--

CREATE TABLE `tipo_congreso` (
  `id_tipo` int(11) NOT NULL,
  `tipo_congreso` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_congreso`
--

INSERT INTO `tipo_congreso` (`id_tipo`, `tipo_congreso`) VALUES
(1, 'Académicos'),
(2, 'Científicos'),
(9, 'COCO'),
(10, 'COCO2'),
(11, 'COCO3'),
(8, 'Culturales'),
(5, 'Empresariales'),
(3, 'Médicos'),
(6, 'Políticos'),
(12, 'prueba builder'),
(7, 'Sociales'),
(4, 'Tecnológicos'),
(13, 'tipo prueba congreso video');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_usuario`
--

CREATE TABLE `tipo_usuario` (
  `id_tipo` int(11) NOT NULL,
  `tipo_usuario` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_usuario`
--

INSERT INTO `tipo_usuario` (`id_tipo`, `tipo_usuario`) VALUES
(2, 'administrador'),
(1, 'normal');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL,
  `usuario` varchar(40) DEFAULT NULL,
  `nombre` varchar(40) DEFAULT NULL,
  `contraseña` varchar(40) DEFAULT NULL,
  `tipo_usuario` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `usuario`, `nombre`, `contraseña`, `tipo_usuario`) VALUES
(1, 'usuario', 'Heider Leyton', 'qwe', 2),
(2, 'mal0112', 'Malfaida Cruz', 'mal2020', 2),
(3, 'ian0406', 'Ian Beltran', 'ian1004', 2),
(4, 'user', 'Luisa Arango', '123', 1),
(5, 'cam0398', 'Camila Ruiz', 'camr7809', 1),
(6, 'lau1405', 'Laura Hurtado', 'lauh0599', 1),
(7, 'clauh12', 'Claudia Hermandez', 'clau12', 1),
(9, 'coco', 'coco', '123', 1),
(10, 'coco2', 'coco2', 'coco2', 1),
(11, 'lolo', 'Uruguay', '123', 1),
(14, 'juan', 'juan', '123', 1),
(15, 'nami', 'nami', '123', 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `acta_edicion`
--
ALTER TABLE `acta_edicion`
  ADD PRIMARY KEY (`id_acta`),
  ADD UNIQUE KEY `id_acta` (`id_acta`),
  ADD KEY `id_ciudad` (`id_ciudad`),
  ADD KEY `id_congreso` (`id_congreso`);

--
-- Indices de la tabla `articulo`
--
ALTER TABLE `articulo`
  ADD PRIMARY KEY (`id_articulo`),
  ADD UNIQUE KEY `id_articulo` (`id_articulo`),
  ADD KEY `tipo_articulo` (`tipo_articulo`);

--
-- Indices de la tabla `articulo_acta`
--
ALTER TABLE `articulo_acta`
  ADD PRIMARY KEY (`id_articulo`,`id_acta`),
  ADD KEY `id_acta` (`id_acta`);

--
-- Indices de la tabla `articulo_edicion`
--
ALTER TABLE `articulo_edicion`
  ADD PRIMARY KEY (`id_articulo`,`numero_edicion`),
  ADD KEY `numero_edicion` (`numero_edicion`);

--
-- Indices de la tabla `articulo_informe_tecnico`
--
ALTER TABLE `articulo_informe_tecnico`
  ADD PRIMARY KEY (`id_articulo`,`numero_informe`),
  ADD KEY `numero_informe` (`numero_informe`);

--
-- Indices de la tabla `articulo_investigador`
--
ALTER TABLE `articulo_investigador`
  ADD PRIMARY KEY (`id_articulo`,`id_investigador`),
  ADD KEY `id_investigador` (`id_investigador`);

--
-- Indices de la tabla `centro`
--
ALTER TABLE `centro`
  ADD PRIMARY KEY (`id_centro`),
  ADD UNIQUE KEY `id_centro` (`id_centro`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `id_ciudad` (`id_ciudad`),
  ADD KEY `id_tema` (`id_tema`);

--
-- Indices de la tabla `ciudad`
--
ALTER TABLE `ciudad`
  ADD PRIMARY KEY (`id_ciudad`),
  ADD UNIQUE KEY `id_ciudad` (`id_ciudad`),
  ADD KEY `id_pais` (`id_pais`);

--
-- Indices de la tabla `congreso`
--
ALTER TABLE `congreso`
  ADD PRIMARY KEY (`id_congreso`),
  ADD UNIQUE KEY `id_congreso` (`id_congreso`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `tipo` (`tipo`);

--
-- Indices de la tabla `edicion`
--
ALTER TABLE `edicion`
  ADD PRIMARY KEY (`numero_edicion`),
  ADD UNIQUE KEY `numero_edicion` (`numero_edicion`),
  ADD KEY `id_revista` (`id_revista`);

--
-- Indices de la tabla `genero`
--
ALTER TABLE `genero`
  ADD PRIMARY KEY (`id_genero`),
  ADD UNIQUE KEY `id_genero` (`id_genero`),
  ADD UNIQUE KEY `genero` (`genero`);

--
-- Indices de la tabla `informe_tecnico`
--
ALTER TABLE `informe_tecnico`
  ADD PRIMARY KEY (`numero_informe`),
  ADD UNIQUE KEY `numero_informe` (`numero_informe`),
  ADD KEY `tema` (`tema`),
  ADD KEY `id_centro` (`id_centro`);

--
-- Indices de la tabla `investigador`
--
ALTER TABLE `investigador`
  ADD PRIMARY KEY (`id_investigador`),
  ADD UNIQUE KEY `id_investigador` (`id_investigador`),
  ADD KEY `centro` (`centro`),
  ADD KEY `genero` (`genero`);

--
-- Indices de la tabla `pais`
--
ALTER TABLE `pais`
  ADD PRIMARY KEY (`id_pais`),
  ADD UNIQUE KEY `id_pais` (`id_pais`);

--
-- Indices de la tabla `palabra`
--
ALTER TABLE `palabra`
  ADD PRIMARY KEY (`id_palabra`),
  ADD UNIQUE KEY `id_palabra` (`id_palabra`),
  ADD UNIQUE KEY `palabra` (`palabra`);

--
-- Indices de la tabla `palabra_clave`
--
ALTER TABLE `palabra_clave`
  ADD PRIMARY KEY (`id_articulo`,`id_palabra`),
  ADD KEY `id_palabra` (`id_palabra`);

--
-- Indices de la tabla `pregunta_recuperacion`
--
ALTER TABLE `pregunta_recuperacion`
  ADD PRIMARY KEY (`id_pregunta`),
  ADD UNIQUE KEY `id_pregunta` (`id_pregunta`),
  ADD UNIQUE KEY `pregunta` (`pregunta`);

--
-- Indices de la tabla `prestamo`
--
ALTER TABLE `prestamo`
  ADD PRIMARY KEY (`id_prestamo`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_articulo` (`id_articulo`);

--
-- Indices de la tabla `recuperacion_contrasena`
--
ALTER TABLE `recuperacion_contrasena`
  ADD PRIMARY KEY (`id_usuario`,`id_pregunta`),
  ADD KEY `id_pregunta` (`id_pregunta`);

--
-- Indices de la tabla `revista`
--
ALTER TABLE `revista`
  ADD PRIMARY KEY (`id_revista`),
  ADD UNIQUE KEY `id_revista` (`id_revista`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `tema` (`tema`);

--
-- Indices de la tabla `tema`
--
ALTER TABLE `tema`
  ADD PRIMARY KEY (`id_tema`),
  ADD UNIQUE KEY `id_tema` (`id_tema`),
  ADD UNIQUE KEY `tema` (`tema`);

--
-- Indices de la tabla `tema_investigador`
--
ALTER TABLE `tema_investigador`
  ADD PRIMARY KEY (`id_investigador`,`id_tema`),
  ADD KEY `id_tema` (`id_tema`);

--
-- Indices de la tabla `tipo_articulo`
--
ALTER TABLE `tipo_articulo`
  ADD PRIMARY KEY (`id_tipo`),
  ADD UNIQUE KEY `id_tipo` (`id_tipo`),
  ADD UNIQUE KEY `tipo_articulo` (`tipo_articulo`);

--
-- Indices de la tabla `tipo_congreso`
--
ALTER TABLE `tipo_congreso`
  ADD PRIMARY KEY (`id_tipo`),
  ADD UNIQUE KEY `id_tipo` (`id_tipo`),
  ADD UNIQUE KEY `tipo_congreso` (`tipo_congreso`);

--
-- Indices de la tabla `tipo_usuario`
--
ALTER TABLE `tipo_usuario`
  ADD PRIMARY KEY (`id_tipo`),
  ADD UNIQUE KEY `id_tipo` (`id_tipo`),
  ADD UNIQUE KEY `tipo_usuario` (`tipo_usuario`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `id_usuario` (`id_usuario`),
  ADD UNIQUE KEY `usuario` (`usuario`),
  ADD KEY `tipo_usuario` (`tipo_usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `acta_edicion`
--
ALTER TABLE `acta_edicion`
  MODIFY `id_acta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `articulo`
--
ALTER TABLE `articulo`
  MODIFY `id_articulo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT de la tabla `centro`
--
ALTER TABLE `centro`
  MODIFY `id_centro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de la tabla `ciudad`
--
ALTER TABLE `ciudad`
  MODIFY `id_ciudad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT de la tabla `congreso`
--
ALTER TABLE `congreso`
  MODIFY `id_congreso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT de la tabla `edicion`
--
ALTER TABLE `edicion`
  MODIFY `numero_edicion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT de la tabla `genero`
--
ALTER TABLE `genero`
  MODIFY `id_genero` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `informe_tecnico`
--
ALTER TABLE `informe_tecnico`
  MODIFY `numero_informe` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT de la tabla `investigador`
--
ALTER TABLE `investigador`
  MODIFY `id_investigador` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT de la tabla `pais`
--
ALTER TABLE `pais`
  MODIFY `id_pais` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `palabra`
--
ALTER TABLE `palabra`
  MODIFY `id_palabra` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `pregunta_recuperacion`
--
ALTER TABLE `pregunta_recuperacion`
  MODIFY `id_pregunta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `prestamo`
--
ALTER TABLE `prestamo`
  MODIFY `id_prestamo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT de la tabla `revista`
--
ALTER TABLE `revista`
  MODIFY `id_revista` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de la tabla `tema`
--
ALTER TABLE `tema`
  MODIFY `id_tema` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT de la tabla `tipo_articulo`
--
ALTER TABLE `tipo_articulo`
  MODIFY `id_tipo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `tipo_congreso`
--
ALTER TABLE `tipo_congreso`
  MODIFY `id_tipo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `tipo_usuario`
--
ALTER TABLE `tipo_usuario`
  MODIFY `id_tipo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `acta_edicion`
--
ALTER TABLE `acta_edicion`
  ADD CONSTRAINT `acta_edicion_ibfk_1` FOREIGN KEY (`id_ciudad`) REFERENCES `ciudad` (`id_ciudad`),
  ADD CONSTRAINT `acta_edicion_ibfk_2` FOREIGN KEY (`id_congreso`) REFERENCES `congreso` (`id_congreso`);

--
-- Filtros para la tabla `articulo`
--
ALTER TABLE `articulo`
  ADD CONSTRAINT `articulo_ibfk_1` FOREIGN KEY (`tipo_articulo`) REFERENCES `tipo_articulo` (`id_tipo`);

--
-- Filtros para la tabla `articulo_acta`
--
ALTER TABLE `articulo_acta`
  ADD CONSTRAINT `articulo_acta_ibfk_1` FOREIGN KEY (`id_articulo`) REFERENCES `articulo` (`id_articulo`),
  ADD CONSTRAINT `articulo_acta_ibfk_2` FOREIGN KEY (`id_acta`) REFERENCES `acta_edicion` (`id_acta`);

--
-- Filtros para la tabla `articulo_edicion`
--
ALTER TABLE `articulo_edicion`
  ADD CONSTRAINT `articulo_edicion_ibfk_1` FOREIGN KEY (`id_articulo`) REFERENCES `articulo` (`id_articulo`),
  ADD CONSTRAINT `articulo_edicion_ibfk_2` FOREIGN KEY (`numero_edicion`) REFERENCES `edicion` (`numero_edicion`);

--
-- Filtros para la tabla `articulo_informe_tecnico`
--
ALTER TABLE `articulo_informe_tecnico`
  ADD CONSTRAINT `articulo_informe_tecnico_ibfk_1` FOREIGN KEY (`id_articulo`) REFERENCES `articulo` (`id_articulo`),
  ADD CONSTRAINT `articulo_informe_tecnico_ibfk_2` FOREIGN KEY (`numero_informe`) REFERENCES `informe_tecnico` (`numero_informe`);

--
-- Filtros para la tabla `articulo_investigador`
--
ALTER TABLE `articulo_investigador`
  ADD CONSTRAINT `articulo_investigador_ibfk_1` FOREIGN KEY (`id_articulo`) REFERENCES `articulo` (`id_articulo`),
  ADD CONSTRAINT `articulo_investigador_ibfk_2` FOREIGN KEY (`id_investigador`) REFERENCES `investigador` (`id_investigador`);

--
-- Filtros para la tabla `centro`
--
ALTER TABLE `centro`
  ADD CONSTRAINT `centro_ibfk_1` FOREIGN KEY (`id_ciudad`) REFERENCES `ciudad` (`id_ciudad`),
  ADD CONSTRAINT `centro_ibfk_2` FOREIGN KEY (`id_tema`) REFERENCES `tema` (`id_tema`);

--
-- Filtros para la tabla `ciudad`
--
ALTER TABLE `ciudad`
  ADD CONSTRAINT `ciudad_ibfk_1` FOREIGN KEY (`id_pais`) REFERENCES `pais` (`id_pais`);

--
-- Filtros para la tabla `congreso`
--
ALTER TABLE `congreso`
  ADD CONSTRAINT `congreso_ibfk_1` FOREIGN KEY (`tipo`) REFERENCES `tipo_congreso` (`id_tipo`);

--
-- Filtros para la tabla `edicion`
--
ALTER TABLE `edicion`
  ADD CONSTRAINT `edicion_ibfk_1` FOREIGN KEY (`id_revista`) REFERENCES `revista` (`id_revista`);

--
-- Filtros para la tabla `informe_tecnico`
--
ALTER TABLE `informe_tecnico`
  ADD CONSTRAINT `informe_tecnico_ibfk_1` FOREIGN KEY (`tema`) REFERENCES `tema` (`id_tema`),
  ADD CONSTRAINT `informe_tecnico_ibfk_2` FOREIGN KEY (`id_centro`) REFERENCES `centro` (`id_centro`);

--
-- Filtros para la tabla `investigador`
--
ALTER TABLE `investigador`
  ADD CONSTRAINT `investigador_ibfk_1` FOREIGN KEY (`centro`) REFERENCES `centro` (`id_centro`),
  ADD CONSTRAINT `investigador_ibfk_2` FOREIGN KEY (`genero`) REFERENCES `genero` (`id_genero`);

--
-- Filtros para la tabla `palabra_clave`
--
ALTER TABLE `palabra_clave`
  ADD CONSTRAINT `palabra_clave_ibfk_1` FOREIGN KEY (`id_articulo`) REFERENCES `articulo` (`id_articulo`),
  ADD CONSTRAINT `palabra_clave_ibfk_2` FOREIGN KEY (`id_palabra`) REFERENCES `palabra` (`id_palabra`);

--
-- Filtros para la tabla `prestamo`
--
ALTER TABLE `prestamo`
  ADD CONSTRAINT `prestamo_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  ADD CONSTRAINT `prestamo_ibfk_2` FOREIGN KEY (`id_articulo`) REFERENCES `articulo` (`id_articulo`);

--
-- Filtros para la tabla `recuperacion_contrasena`
--
ALTER TABLE `recuperacion_contrasena`
  ADD CONSTRAINT `recuperacion_contrasena_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  ADD CONSTRAINT `recuperacion_contrasena_ibfk_2` FOREIGN KEY (`id_pregunta`) REFERENCES `pregunta_recuperacion` (`id_pregunta`);

--
-- Filtros para la tabla `revista`
--
ALTER TABLE `revista`
  ADD CONSTRAINT `revista_ibfk_1` FOREIGN KEY (`tema`) REFERENCES `tema` (`id_tema`);

--
-- Filtros para la tabla `tema_investigador`
--
ALTER TABLE `tema_investigador`
  ADD CONSTRAINT `tema_investigador_ibfk_1` FOREIGN KEY (`id_investigador`) REFERENCES `investigador` (`id_investigador`),
  ADD CONSTRAINT `tema_investigador_ibfk_2` FOREIGN KEY (`id_tema`) REFERENCES `tema` (`id_tema`);

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`tipo_usuario`) REFERENCES `tipo_usuario` (`id_tipo`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
