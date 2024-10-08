import tobii_research as tr
import time
import sys
import math
                              
                              
def gaze_data_callback(gaze_data):
    message = '{}; {}, {}, {}, {}, {}; {}, {}, {}, {}, {}'.format(
        round(time.time() * 1000),
        gaze_data['left_gaze_point_on_display_area'][0],
        gaze_data['left_gaze_point_on_display_area'][1],
        gaze_data['left_gaze_point_validity'],
        gaze_data['left_pupil_diameter'],
        gaze_data['left_pupil_validity'],
        gaze_data['right_gaze_point_on_display_area'][0],
        gaze_data['right_gaze_point_on_display_area'][1],
        gaze_data['right_gaze_point_validity'],
        gaze_data['right_pupil_diameter'],
        gaze_data['right_pupil_validity']
     )
sys.stdout.flush()
                              
found_eyetrackers = tr.find_all_eyetrackers()
print(found_eyetrackers)
my_eyetracker = found_eyetrackers[0]
print(my_eyetracker.model)
#print(my_eyetracker.device_name)
my_eyetracker.subscribe_to(tr.EYETRACKER_GAZE_DATA, gaze_data_callback, as_dictionary=True)
start_time = time.time()
while time.time() - start_time <= math.inf:
   continue