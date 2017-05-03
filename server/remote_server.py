from flask import Flask, request, jsonify
# from urllib.request import urlopen

app = Flask(__name__)

devices = {};

@app.route('/')
def test():
    return "fuck you"

@app.route('/piRegister', methods=['POST'])
def register():
    if request.method != 'POST':
        return jsonify({'data' : 'incorrect request type', 'result' : 'failure'})

    if device_name in devices:
        return jsonify({'result' : 'failure', 'data' : 'device name already exists'})
    data = request.get_json()
    device_ip = data['ip']
    device_name = data['device_name']
    devices[device_name] = device_ip

    return jsonify({'result' : 'success'})

@app.route('/toggle', methods=['POST'])
def toggle():
    if request.method != 'POST':
        return jsonify({'data' : 'incorrect request type', 'result' : 'failure'})

    data = request.get_json()
    toggle_state = data['state']
    target_device = data['device_name']
    print ("got state change request to: " + toggle_state + " for device: " + target_device)
    return jsonify({'result' : 'success'})
    # TODO send request to pi server

@app.route('/androidRegister', methods = ['POST'])
def androidRegister():
    if request.method != 'POST':
        return jsonify({'data' : 'incorrect request type', 'result' : 'failure'})

    data = request.get_json()
    device_name = data['device_name']
    if device_name in devices:
        return jsonify({'result' : 'success', 'data' : 'exists'})
    else:
        return jsonify({'result' : 'success', 'data' : 'none'})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000, debug=False)
