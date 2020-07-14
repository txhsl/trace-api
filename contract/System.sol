pragma solidity >=0.4.22 <0.6.0;
import './Role.sol';
import './Data.sol';

contract System {

    struct User {
        string role;
        uint level;
    }

    mapping(address => User) private users;
    mapping(string => Role) private rcIndex;
    mapping(string => Data) private dcIndex;
    address private admin;

    event user(address _user, string _roleName);
    event role(string _roleName, Role _rc, address _admin);
    event data(string _dcName, Data _dc, address _admin);
    event transfer(address _from, address _to, uint _amount);

    constructor() public {
        admin = msg.sender;
    }

    function register(address _user, string memory _roleName) public {
        require(msg.sender == rcIndex[_roleName].getAdmin(), "Permission denied.");
        users[_user] = User({role:_roleName, level:500});
        emit user(_user, _roleName);
    }

    function addRC(string memory _roleName, address _admin) public {
        require(msg.sender == admin, "Permission denied.");
        Role rc = new Role(_admin);
        rcIndex[_roleName] = rc;
        emit role(_roleName, rc, _admin);
    }

    function addDC(string memory _dcName, address _admin) public {
        require(msg.sender == admin, "Permission denied.");
        Data dc = new Data(_admin, _dcName);
        dcIndex[_dcName] = dc;
        emit data(_dcName, dc, _admin);
    }

    function getDC(string memory _dcName) public view returns(address) {
        return address(dcIndex[_dcName]);
    }

    function getRC(string memory _rcName) public view returns(address) {
        return address(rcIndex[_rcName]);
    }

    function getRole(address _user) public view returns(string memory) {
        return users[_user].role;
    }

    function getLevel(address _user) public view returns(uint) {
        return users[_user].level;
    }

    function assignReader(string memory _dcName, string memory _roleName) public {
        require(dcIndex[_dcName].getAdmin() == msg.sender, "Permission denied.");
        rcIndex[_roleName].addManaged(_dcName, address(dcIndex[_dcName]));
    }

    function assignWriter(string memory _dcName, string memory _roleName) public {
        require(dcIndex[_dcName].getAdmin() == msg.sender, "Permission denied.");
        rcIndex[_roleName].addOwned(_dcName, address(dcIndex[_dcName]));
    }

    function checkReader(string memory _dcName, address _user) public view returns(bool) {
        if(rcIndex[users[_user].role].getManaged(_dcName) == address(dcIndex[_dcName])) {
            return true;
        }
        else {
            return false;
        }
    }

    function checkWriter(string memory _dcName, address _user) public view returns(bool) {
        if(rcIndex[users[_user].role].getOwned(_dcName) == address(dcIndex[_dcName])) {
            return true;
        }
        else {
            return false;
        }
    }

    function readData(string memory _dcName, string memory _dataId) public view returns(string memory) {
        require(checkReader(_dcName, msg.sender), "Permission denied.");
        return dcIndex[_dcName].read(_dataId);
    }

    function writeData(string memory _dcName, string memory _fileId, string memory _hash) public {
        require(checkWriter(_dcName, msg.sender), "Permission denied.");
        dcIndex[_dcName].write(_fileId, _hash);
    }

    function transferFrom(address _from, address _to, uint _amount) public {
        require(msg.sender == admin || msg.sender == rcIndex[users[_from].role].getAdmin(), "Permission denied.");
        if (users[_from].level < _amount) {
            uint available = users[_from].level;
            users[_from].level -= available;
            users[_to].level += available;
        }
        else {
            users[_from].level -= _amount;
            users[_to].level += _amount;
        }
        emit transfer(_from, _to, _amount);
    }
}