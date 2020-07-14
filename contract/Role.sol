pragma solidity >=0.4.22 <0.6.0;
import './System.sol';

contract Role {

    mapping(string => address) private owned;
    mapping(string => address) private managed;
    address private admin;
    address private sysAddr;

    event own(address _self, string _name, address _scAddr);
    event manage(address _self, string _name, address _scAddr);

    constructor(address _admin) public {
        sysAddr = msg.sender;
        admin = _admin;
    }

    function getAdmin() public view returns (address) {
        return admin;
    }

    function addOwned(string memory _name, address _scAddr) public {
        require(msg.sender == sysAddr, "Permission denied.");
        owned[_name] = _scAddr;
        emit own(address(this), _name, _scAddr);
    }

    function getOwned(string memory _name) public view returns (address) {
        return owned[_name];
    }

    function addManaged(string memory _name, address _scAddr) public {
        require(msg.sender == sysAddr, "Permission denied.");
        managed[_name] = _scAddr;
        emit manage(address(this), _name, _scAddr);
    }

    function getManaged(string memory _name) public view returns (address) {
        return managed[_name];
    }
}