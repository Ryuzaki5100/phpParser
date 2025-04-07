package com.example.demo;

import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PhpParserBridge {
    public static String runPhpParser(String phpCode) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("php", "php/parse.php");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.write(phpCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        process.waitFor();
        return output.toString();
    }

    public static void main(String[] args) throws Exception {
        String phpCode = "<?php\n" +
                "/**\n" +
                " * Start Screen\n" +
                " * \n" +
                " * @package    Ink DCS Presentation Layer\n" +
                " * @copyright  Copyright(c) Ink Aviation {@link http://inkav.com}\n" +
                " */\n" +
                "// git test\n" +
                "include \"configuration.php\";\n" +
                " \n" +
                "use Helpers\\Adaptors\\CommonAdaptor;\n" +
                " \n" +
                "this_server::redirect_to_node_server($PHP_SELF);\n" +
                " \n" +
                "anti_xss::validate_vars(array(\n" +
                "  'adaptor_port'                 => array('type' => 'int'),\n" +
                "  'set_company'                  => array('type' => 'int'),\n" +
                "  'new_user_key'                 => array('type' => 'string'),\n" +
                "  'get_user_session_inactivity'  => array('type' => 'int'),\n" +
                "  'default_station'              => array('type' => 'int'),\n" +
                "  'update_default_bin'           => array('type' => 'int'),\n" +
                "  'value'                        => array('type' => 'any'),\n" +
                "  'enable_mouse'                 => array('type' => 'int'),\n" +
                "  'get_machine_keys'             => array('type' => 'int'),\n" +
                "  'station_key'                  => array('type' => 'string'),\n" +
                "  'ping'                         => array('type' => 'int'),\n" +
                "  'machine_key'                  => array('type' => 'string'),\n" +
                "  'peripheral_key'               => array('type' => 'string'),\n" +
                "  'set_sita_default_atb'         => array('type' => 'int'),\n" +
                "  'set_sita_default_btp'         => array('type' => 'int'),\n" +
                "  'set_sita_default_dcp'         => array('type' => 'int'),\n" +
                "  'set_sita_default_msr'         => array('type' => 'int'),\n" +
                "  'set_sita_default_ocr'         => array('type' => 'int'),\n" +
                "  'set_sita_bgr_name'            => array('type' => 'int'),\n" +
                "  'device_name'                  => array('type' => 'string'),\n" +
                "  'set_default'                  => array('type' => 'int'),\n" +
                "  'set_sita_lsr_name'            => array('type' => 'int'),\n" +
                "  'set_sita_default_peripherals' => array('type' => 'int'),\n" +
                "  'atb'                          => array('type' => 'string'),\n" +
                "  'btp'                          => array('type' => 'string'),\n" +
                "  'msr'                          => array('type' => 'string'),\n" +
                "  'ocr'                          => array('type' => 'string'),\n" +
                "  'dcp'                          => array('type' => 'string'),\n" +
                "  'save_default_sita_names'      => array('type' => 'int'),\n" +
                "  'type'                         => array('type' => 'string'),\n" +
                "  'set_arinc_default_atb'        => array('type' => 'int'),\n" +
                "  'set_arinc_default_btp'        => array('type' => 'int'),\n" +
                "  'set_arinc_default_dcp'        => array('type' => 'int'),\n" +
                "  'set_arinc_default_msr'        => array('type' => 'int'),\n" +
                "  'set_arinc_default_ocr'        => array('type' => 'int'),\n" +
                "  'set_arinc_bgr_name'           => array('type' => 'int'),\n" +
                "  'set_arinc_bcr_name'           => array('type' => 'int'),\n" +
                "  'save_user_session_sita_debug' => array('type' => 'int'),\n" +
                "  'debug_data'                   => array('type' => 'string'),\n" +
                "  'set_preference'               => array('type' => 'int'),\n" +
                "  'force_home'                   => array('type' => 'int'),\n" +
                "  '_'                            => array('type' => 'string'),\n" +
                "  'login_success'                => array('type' => 'string'),\n" +
                "));\n" +
                " \n" +
                "$nav_cache_name = PROJECT_NAME . \"navigation_\" . $live_user_session->live_key;\n" +
                " \n" +
                "if (XCACHE)\n" +
                "{\n" +
                "xcache_unset($nav_cache_name);\n" +
                "}\n" +
                " \n" +
                "CommonAdaptor::setPortCookie($vars->adaptor_port);\n" +
                " \n" +
                "if ($vars->set_company && $vars->new_user_key)\n" +
                "{\n" +
                "  $live_user_session->change_login($vars->new_user_key, $live_user_session->live_user_key);\n" +
                "  //$live_user_session->load();\n" +
                "  //go_to_url_top('home.php');\n" +
                "  exit();\n" +
                "}\n" +
                " \n" +
                "if ($vars->get_user_session_inactivity)\n" +
                "{\n" +
                "  print $live_user_session->get_inactivity_seconds();\n" +
                "  exit;\n" +
                "}\n" +
                " \n" +
                "//var_dump($vars);var_dump($live_user_session);die();\n" +
                "/** @var home_page_controller $controller */\n" +
                "$controller = row::get_instance(\"home_page_controller\", $db, $vars);\n" +
                "$controller->live_user_session = $live_user_session;\n" +
                "$controller->live_user         = $live_user;\n" +
                "$controller->set_model_object(\"flight\");\n" +
                "$controller->set_search_destination($PHP_SELF);\n" +
                "if ($vars->default_station) $_SESSION[\"menu_station\"] = $_SESSION[\"default_station\"];\n" +
                "$controller->process_form_data();\n" +
                "$controller->go_to_destination();\n" +
                " \n" +
                "//----------------------AJAX SECTION ---------------------------//\n" +
                "if ($vars->update_default_bin)\n" +
                "{\n" +
                "  $live_user_session->default_bin = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  \n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->enable_mouse)\n" +
                "{\n" +
                "  $live_user_session->is_mouse_enabled = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  \n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->get_machine_keys && $vars->station_key)\n" +
                "{\n" +
                "  header(\"Content-type: text/javascript; charset: UTF-8\", true);\n" +
                "  $machine_details = machine::get_machines_details($db, $vars->station_key, NULL, 0, NULL, true, ['KIOSK']);\n" +
                "  //print \"<pre>$machine_details</pre>\";\n" +
                "  $a_return        = array();\n" +
                "  $a_return[]      = \"\";\n" +
                "  $a_return[]      = translate($db, LANGUAGE_KEY, 'PLEASE_CHOOSE');\n" +
                " \n" +
                "  if (is_array($machine_details)) foreach ($machine_details as $machine_detail)\n" +
                "  {\n" +
                "    $a_return[] = $machine_detail['machine_key'];\n" +
                "    $a_return[] = $machine_detail['summary'] . \" - \" . $machine_detail[\"machine_name\"];\n" +
                "  }\n" +
                "  \n" +
                "  print \"'1'&'Success'&'\" . implode(\"|\", $a_return) . \"'\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->ping)\n" +
                "{\n" +
                "  /** @var machine $machine */\n" +
                "  $machine         = row::get_instance(\"machine\", $db, $vars, $vars->machine_key);\n" +
                "  $result          = $machine->ping_peripheral($vars->peripheral_key);\n" +
                "  print \"'1'&'Success'&\".$result;\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_sita_default_atb && $vars->value)\n" +
                "{\n" +
                "  $live_user_session->default_cute_atb = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_sita_default_btp && $vars->value)\n" +
                "{\n" +
                "  $live_user_session->default_cute_btp = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_sita_default_dcp && $vars->value)\n" +
                "{\n" +
                "  $live_user_session->default_cute_dcp = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_sita_default_msr && $vars->value)\n" +
                "{\n" +
                "  $live_user_session->default_cute_msr = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_sita_default_ocr && $vars->value)\n" +
                "{\n" +
                "  $live_user_session->default_cute_ocr = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_sita_bgr_name && $vars->device_name)\n" +
                "{\n" +
                "  $live_user_session->set_sita_bgr_name($vars->device_name, $vars->set_default);\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_sita_lsr_name && $vars->device_name)\n" +
                "{\n" +
                "  $live_user_session->set_sita_lsr_name($vars->device_name, $vars->set_default);\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_sita_default_peripherals)\n" +
                "{\n" +
                "  if ($vars->atb) $live_user_session->default_cute_atb = $vars->atb;\n" +
                "  if ($vars->btp) $live_user_session->default_cute_btp = $vars->btp;\n" +
                "  if ($vars->msr) $live_user_session->default_cute_msr = $vars->msr;\n" +
                "  if ($vars->ocr) $live_user_session->default_cute_ocr = $vars->ocr;\n" +
                "  if ($vars->dcp) $live_user_session->default_cute_dcp = $vars->dcp;\n" +
                "  \n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                " \n" +
                "if ($vars->save_default_sita_names && $vars->type)\n" +
                "{\n" +
                "  if ($vars->type == 'atb' || $vars->type == 'btp' || $vars->type == 'bgr')\n" +
                "  {\n" +
                "    if ($vars->type == 'atb') $property = 'sita_default_atb';\n" +
                "    if ($vars->type == 'btp') $property = 'sita_default_btp';\n" +
                "    if ($vars->type == 'bgr') $property = 'sita_default_bgr';\n" +
                "    \n" +
                "    $live_user_session->$property = $vars->value;\n" +
                "    $live_user_session->save();\n" +
                "  }\n" +
                "}\n" +
                " \n" +
                "//arinc \n" +
                "if ($vars->set_arinc_default_atb && $vars->value)\n" +
                "{\n" +
                "  $live_user_session->default_cute_atb = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_arinc_default_btp && $vars->value)\n" +
                "{\n" +
                "  $live_user_session->default_cute_btp = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_arinc_default_dcp && $vars->value)\n" +
                "{\n" +
                "  $live_user_session->default_cute_dcp = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_arinc_default_msr && $vars->value)\n" +
                "{\n" +
                "  $live_user_session->default_cute_msr = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_arinc_default_ocr && $vars->value)\n" +
                "{\n" +
                "  $live_user_session->default_cute_ocr = $vars->value;\n" +
                "  $live_user_session->save();\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_arinc_bgr_name && $vars->device_name)\n" +
                "{\n" +
                "  $live_user_session->set_arinc_bgr_name($vars->device_name, $vars->set_default);\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "if ($vars->set_arinc_bcr_name && $vars->device_name)\n" +
                "{\n" +
                "  $live_user_session->set_arinc_bcr_name($vars->device_name, $vars->set_default);\n" +
                "  print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                "elseif ($vars->save_user_session_sita_debug && $vars->debug_data)\n" +
                "{\n" +
                "  $live_user_session->save_user_session_sita_debug($vars->debug_data);\n" +
                "print \"'1'&'Success'&ok\";\n" +
                "  exit;\n" +
                "}\n" +
                " \n" +
                " \n" +
                " \n" +
                "//--------------------- Display form ----------------------//\n" +
                " \n" +
                "$controller->set_language($language);\n" +
                "$controller->set_meta_data();\n" +
                "$controller->set_title(\"HOME_PAGE\");\n" +
                "$controller->set_order_by(\"estimated_departure_date, estimated_departure_time\");\n" +
                "$controller->set_view_template(VIEW_TEMPLATE_CUSTOM_PATH . \"/home_page_view_template.php\");\n" +
                "$controller->search_engine->set_rows_to_return(FLIGHTS_ON_HOME_PAGE);\n" +
                "//$controller->set_custom_javascript(\"home_page_javascript.js\");\n" +
                "$controller->factory_view();\n" +
                " \n" +
                " \n" +
                "?>";

        String jsonOutput = runPhpParser(phpCode);

        // Just for viewing
        System.out.println("AST JSON: \n" + jsonOutput);

        // TODO: Parse jsonOutput into PhpFileStructure using Gson and custom logic (manual mapping)
        // For now, just printing raw output
    }
}