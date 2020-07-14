pragma solidity >=0.4.22 <0.6.0;
import './System.sol';

contract Data {

    string private name;

    mapping(string => string) private hashs;
    address private admin;
    address private sysAddr;

    event data(address _self, string _fileId, string _hash);

    constructor(address _admin, string memory _name) public {
        sysAddr = msg.sender;
        admin = _admin;
        name = _name;
    }

    function getFileNum(string memory _id) public pure returns (string memory) {
        bytes(_id)[bytes(_id).length - 1] = '0';
        bytes(_id)[bytes(_id).length - 2] = '0';

        return _id;
    }

    function getAdmin() public view returns (address) {
        return admin;
    }

    function write(string memory _fileId, string memory _hash) public {
        require(msg.sender == sysAddr, "Permission denied.");
        hashs[getFileNum(_fileId)] = _hash;
        emit data(address(this), _fileId, hashs[getFileNum(_fileId)]);
    }

    function read(string memory _dataId) public view returns (string memory) {
        require(msg.sender == sysAddr, "Permission denied.");
        return hashs[getFileNum(_dataId)];
    }
}