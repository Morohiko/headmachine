#include <unistd.h>
#include <assert.h>
#include <string.h>

#include "log.h"
#include "core.h"
#include "controller.h"
#include "wifi.h"
#include "utils.h"

static int main_loop(machine_controller *controller,
		     struct connection_info *conn_info_gyroscope,
		     struct connection_info_cam *conn_info_camera) {
    int status = 0;

    while (1) {
//        print("DEBUG: main loop");
#ifdef ENABLE_CAMERA
        if (controller->camera_current_state != controller->camera_state) {
            if (controller->camera_state == 1) {
                status = start_camera(controller);
            }
            if (controller->camera_state == 0) {
                status = stop_camera(controller);
            }
            assert(!status);
            controller->camera_current_state = controller->camera_state;
            print(DEBUG, "changed camera state to %d", controller->camera_current_state);
        }

        if (controller->camera_transmitter_current_state != controller->camera_transmitter_state) {
            if (controller->camera_transmitter_state == 1) {
                status = start_camera_transmitter(controller, conn_info_camera);
            }
            if (controller->camera_transmitter_state == 0) {
                status = stop_camera_transmitter(controller);
            }
            assert(!status);
            controller->camera_transmitter_current_state = controller->camera_transmitter_state;
            print(DEBUG, "changed camera transmitter state to %d", controller->camera_transmitter_current_state);
        }
#endif // ENABLE_CAMERA
        if (controller->gyroscope_receiver_current_state != controller->gyroscope_receiver_state) {
            if (controller->gyroscope_receiver_state == 1) {
                status = start_gyroscope_data_receiver(controller, conn_info_gyroscope);
            }
            if (controller->gyroscope_receiver_state == 0) {
                status = stop_gyroscope_data_receiver(controller);
            }
            assert(!status);
            controller->gyroscope_receiver_current_state = controller->gyroscope_receiver_state;
            print(DEBUG, "changed gyroscope receiver state to %d", controller->gyroscope_receiver_current_state);
        }
        if (controller->motor_current_state != controller->motor_state) {
            if (controller->motor_state == 1) {
                status = start_motor(controller);
            }
            if (controller->motor_state == 0) {
                status = stop_motor(controller);
            }
            assert(!status);
            controller->motor_current_state = controller->motor_state;
            print(DEBUG, "changed motor state to %d", controller->motor_current_state);
        }
        sleep(1);
    }
}

static int configure_network(struct connection_info *conn_info_controller,
                             struct connection_info *conn_info_gyroscope,
                             struct connection_info_cam *conn_info_camera) {
    assert(conn_info_gyroscope);
    assert(conn_info_camera);

    int retval = 0;
    char ip_addr[16];

#ifdef GET_CUSTOM_IP_FROM_CONFIG
    memcpy(ip_addr, TARGET_IP, 16);
#else // get ip from /proc/net/arp
    retval = get_target_ip_addr(ip_addr);
    if (retval != 0) {
        print(ERROR, "cannot get target ip addr, retval: %d", retval);
        return retval;
    }
#endif // GET_CUSTOM_IP_FROM_CONFIG

#ifdef REMOTE_CONTROLLER
    memcpy(conn_info_controller->local_ip, LOCAL_IP, 16);
    memcpy(conn_info_controller->target_ip, ip_addr, 16);

    assert(conn_info_controller->local_ip);
    assert(conn_info_controller->target_ip);

    conn_info_controller->local_port = LOCAL_CONTROLLER_PORT;
    conn_info_controller->target_port = TARGET_CONTROLLER_PORT;

    print(INFO, "selected controller connection:\n local ip: %s, target ip: %s, local port: %d, target port: %d",
                conn_info_controller->local_ip,
                conn_info_controller->target_ip,
                conn_info_controller->local_port,
                conn_info_controller->target_port);
#endif // REMOTE_CONTROLLER

#ifdef ENABLE_GYROSCOPE_RECEIVER
    memcpy(conn_info_gyroscope->local_ip, LOCAL_IP, 16);
    memcpy(conn_info_gyroscope->target_ip, ip_addr, 16);

    assert(conn_info_gyroscope->local_ip);
    assert(conn_info_gyroscope->target_ip);

    conn_info_gyroscope->local_port = LOCAL_GYROSCOPE_PORT;
    conn_info_gyroscope->target_port = TARGET_GYROSCOPE_PORT;
    print(INFO, "selected gyroscope connection:\n local ip: %s, target ip: %s, local port: %d, target port: %d", 
                 conn_info_gyroscope->local_ip,
                 conn_info_gyroscope->target_ip,
                 conn_info_gyroscope->local_port,
                 conn_info_gyroscope->target_port);
#endif // ENABLE_GYROSCOPE_RECEIVER

#ifdef ENABLE_CAMERA
// install connection info for camera frame
    memcpy(conn_info_camera->local_ip, LOCAL_IP, 16);
    memcpy(conn_info_camera->target_ip, ip_addr, 16);

    assert(conn_info_camera->local_ip);
    assert(conn_info_camera->target_ip);

// configure camera frame port
    conn_info_camera->frame_local_port = LOCAL_CAMERA_FRAME_PORT;
    conn_info_camera->frame_target_port = TARGET_CAMERA_FRAME_PORT;
// configure camera ack port
    conn_info_camera->ack_local_port = LOCAL_CAMERA_ACK_PORT;
    conn_info_camera->ack_target_port = TARGET_CAMERA_ACK_PORT;

    print(INFO, "selected camera frame connection:\n local ip: %s, target ip: %s, frame local port: %d, frame target port: %d, ack local port: %d, ack target port: %d",
		    conn_info_camera->local_ip,
		    conn_info_camera->target_ip,
		    conn_info_camera->frame_local_port,
		    conn_info_camera->frame_target_port,
		    conn_info_camera->ack_local_port,
		    conn_info_camera->ack_target_port);
#endif // ENABLE_CAMERA
    return 0;
}

int main() {
    set_log_level(DEBUG);
    enable_log_with_module_names();
    register_log_module("MAIN_MODULE", pthread_self());

    print(INFO, "======== Start Machinepart =======");

    print(INFO, "======== configure network =======");

    struct connection_info conn_info_controller;
    struct connection_info conn_info_gyroscope;
    struct connection_info_cam conn_info_camera;

    configure_network(&conn_info_controller, &conn_info_gyroscope, &conn_info_camera);

    print(DEBUG, "start remote controller");

    machine_controller controller;

    init_machine_controller_states(&controller);
#ifdef REMOTE_CONTROLLER
    controller.conn = conn_info_controller;
#endif // REMOTE_CONTROLLER
    start_remote_controller(&controller);

    main_loop(&controller, &conn_info_gyroscope, &conn_info_camera);

    return 0;
}
